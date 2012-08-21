<?php

//    dvijok - cms written in gwt
//    Copyright (C) 2010  Pechenko Anton Vladimirovich aka Parilo
//    mailto: forpost78 at gmail dot com
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>

require_once "dvipc.php";
require_once "lib.php";
require_once "db/db.php";
require_once "db/dbinit.php";
require_once 'dvobjfilter.php';
require_once 'config.def.php';

class DVService {

	private $db;
// 	private $root;
	private $expTimeAnon;

	public function __construct(){
		$this->initExpTime();
		$this->initDB();

// 		$this->root['uid'] = 'root';
// 		$this->root['gids'][] = 'root';
	}

	private function initExpTime(){
		global $config;
		$this->expTimeAnon = $config['SESSION_EXPIRATION_TIME_ANON'];
	}

	private function initDB(){
		global $config;
		$this->db = new DataBase($config['dbfilesdir']);
		if( !$this->db->isInitialized() ){
			$dbinit = new DataBaseInit();
			$dbinit->init($this->db);
		}
	}

	public function call($obj, $ip){

		$func = $obj['func'];

		if( $func == 'initSession' ){
			return $this->initSession($obj, $ip);

		} else if( $func == "login" ){
			return $this->checkSession($obj, $ip, 'login');

		} else if( $func == "logout" ){
			return $this->checkSession($obj, $ip, 'logout');

		} else if( $func == "saveUserData" ){
			return $this->checkSession($obj, $ip, 'saveUserData');

		} else if( $func == "listenForEvents" ){
			return $this->checkSession($obj, $ip, 'listenForEvents');

		} else if( $func == "checkSession" ){
			return $this->checkSession($obj, $ip, 'checkSessionFunc');
						
		} else if( $func == "external" ){
			return $this->checkSession($obj, $ip, 'external');

		} else {

			$ret['result'] = 'function not found';
			return $ret;

		}

	}

	/**
	 *
	 * Checks user session
	 * @param DBObject $obj
	 * @param String $ip
	 * @param Method $method - this method will be invoked as method($obj) if session is valid
	 */
	private function checkSession($obj, $ip, $method){

		if( isset($obj['sid']) ){
			$sid = strtolower($obj['sid']);
			
			if( isValidMd5($sid) ){
			
				$sess = $this->db->getObjectByVal('sid', $sid, 'sess'/*, $this->root*/);
				if( $sess === false ) $ifsess = false;
				else {
					//TODO pick correct expiration time for guest and authorized users
					$expTime = $this->expTimeAnon;
					$now = nowMinuts();
					if( ($sess['time'] + $expTime) < $now ) $ifsess = false;
					else {
						$sess['time'] = $now;
						$this->db->putObject_($sess, false/*, $this->root*/);
						$ifsess = true;
					}
	
				}
			
			} else $ifsess = false;
			
		} else $ifsess = false;

		if( $ifsess === false  ){
			$ret['result'] = 'notsid';
			return $ret;
		} else {

			$user = $this->getUser($sess['uid']);
			if( $user === false ) return retarr('notsid'); //return retarr('notuser');
			else {
				$user['ip'] = $ip;
				return $this->$method($obj, $user, $sess);
			}
		}
	}

	private function checkSessionFunc($inp, $user, $sess){
 		$ret['objs']['userinfo'] = isset($user['userinfo'])?$user['userinfo']:array();
 		
 		if( $sess['uid'] == 'guest' ){
 			$ret['objs']['userdata'] = isset($sess['userdata'])?$sess['userdata']:array();
 		} else {
 			$ret['objs']['userdata'] = isset($user['userdata'])?$user['userdata']:array();
 		}
 		
// 		$ret['objs']['login'] = $user['uid'];
		$ret['result'] = "success";
		return $ret;
	}

	private function listenForEvents($inp, $user, $sess){
		
		$need = $inp['obj']['need'];
		if( isset($inp['obj']['qid']) ) $queueid = $inp['obj']['qid'];
		else $queueid = '';
		$sid = $inp['sid'];
		$needsendqid = false;
		if( !isValidMd5($queueid) ){
			$queueid = randHash();
			$needsendqid = true;
		}
		
		$sysipc = new DVIPCSys();
		$useripc = new DVIPCUser($sid);
		$sysipc->registerIPC($sid);
		$useripc->register($queueid);
		$sysevent = $useripc->listenForEvent();
		
		if( $sysevent['type'] == 'idle' ){
			if( $needsendqid ) $sysevent['qid'] = $queueid;
			$ret['objs']['event'] = $sysevent;
			$ret['result'] = 'success';
			return $ret;
		}
		
		//check tags
		$systags = $sysevent['tags'];
		$systagsarr = explode(' ', $systags);
		
		unset($need['_isarr']);
		foreach( $need as $ind => $tags ){
			$needtags = $tags['tags'];
			$needtagsarr = explode(' ', $needtags);
		
			if( count($needtagsarr) > 0 ) if( $needtagsarr == array_values(array_intersect($systagsarr, $needtagsarr)) ){

				$clevent = $sysevent;
				$systagsarr['_isarr'] = '_isarr';
				$clevent['tags'] = $systagsarr;
				
				if( isset($clevent['objs']) ) {
					$filter = new DvObjFilter();
					$clevent['objs'] = $filter->filter($clevent['objs'], $user, $clevent['tags']);
				}

				if( $needsendqid ) $clevent['qid'] = $queueid;
				$ret['objs']['event'] = $clevent;
				$ret['result'] = 'success';
				return $ret;
				
			}
		}
		
		return $this->listenForEvents($inp, $user, $sess);
		
	}
	
	private function removeOutdatedSessions(){
	}

	private function initSession($obj, $ip){

		$sid = randHash();
		$sess['sid'] = $sid;
		$sess['uid'] = 'guest';
		$sess['time'] = nowMinuts();
		$sess['ip'] = $ip;
// 		$sess['userinfo'] = array();

		$this->db->putObject_($sess, 'sess unauth'/*, $this->root*/);
		$retobj['sid'] = $sid;

		$ret['result'] = "success";
		$ret['objs'] = $retobj;
		return $ret;
	}

	private function getUser($uid){
// 		return $this->db->getObjectByVal('uid', $uid, 'user', $this->root);
		return $this->db->getObjectByTags('user '.$uid/*, $this->root*/);
	}

	private function saveUserData($inp, $user, $sess){
		
 		if( $sess['uid'] == 'guest' ){
 			$sess['userdata'] = $inp['userdata'];
 			$this->db->putObject_($sess);
 		} else {
 			$user['userdata'] = $inp['userdata'];
 			$this->db->putObject_($user);
 		}

 		$ret['result'] = "success";
 		return $ret;
	}
	
// 	private function putObject($inp, $user, $sess){

// 		$ret['result'] = 'put object is denied now';
// 		return $ret;

// 		$inp = $inp['obj'];

// 		$uid = $user['uid'];
// 		if( isset($inp['dbo']) ) $obj = $inp['dbo'];
// 		else return retarr('specify DBObject');

// 		if( isset($inp['tags']) ) $tags = $inp['tags'];
// 		else return retarr('specify tags');

// 		if( isset($inp['rights']) ) $rights = $inp['rights'];
// 		else {
// 			$rights['uid'] = $uid;
// 			$rights['gid'] = $uid;
// 			$rights['ur'] = '1';
// 			$rights['uw'] = '1';
// 			$rights['gr'] = '0';
// 			$rights['gw'] = '0';
// 			$rights['or'] = '0';
// 			$rights['ow'] = '0';
// 		}

// 		$id = $this->db->putObject($obj, $tags, $user, $rights);
// 		$obj['id'] = "$id";

// 		$ret['result'] = 'success';
// 		$ret['objs']['id'] = "$id";
// 		return $ret;
// 	}

// 	private function getObjects($inp, $user, $sess){

// 		$inp = $inp['obj'];

// 		if( !isset($inp['tags']) ) return retarr('specify tags');
// 		$tags = $inp['tags'];
// 		if( $tags == "" ) return retarr('specify tags');

// 		if( isset($inp['count']) ) $count = $inp['count'];
// 		else $count = 0;

// 		if( isset($inp['offset']) ) $offset = $inp['offset'];
// 		else $offset = 0;

// 		$objs = $this->db->getObjectsByTags($tags, $user, $count, $offset);
// 		$filter = new DvObjFilter();
// 		$objs = $filter->filter($objs, $user, $tags);
// 		$objs['_isarr'] = '1';
		
// 		$ret['result'] = 'success';
// 		$ret['objs'] = $objs;

// 		return $ret;
// 	}

// 	private function delObject($inp, $user, $sess){

// 		$inp = $inp['obj'];

// 		if( isset($inp['id']) ) $id = $inp['id'];
// 		else return retarr('specify id');

// 		$res = $this->db->delObject($id, $user);
// 		if( $res === true ){
// 			$ret['return'] = 'success';
// 			$ret['id'] = $id;
// 			return $ret;
// 		}
// 		else return retarr($res);

// 	}

	private function login($inp, $user, $sess){
		// need rewriting
/*		$inp = $inp['obj'];

		if( $user['uid'] == 'guest' ){

			if( isset($inp['login']) ){
					
				$login = $inp['login'];
				$loginuser = $this->db->getObjectByVal('uid', $login, 'user', $this->root);
				
				if( !isset($loginuser['nologin']) ){

					if( isset($inp['response']) ){

						$resp = strtolower($inp['response']);
						
						if( isset($sess['chal']) && isset($loginuser['pass']) ){
														
							if( $resp === md5($sess['chal'].$loginuser['pass']) ){

								unset($sess['chal']);
								$sess['uid'] = $loginuser['uid'];
								$this->db->putObject_($sess, 'sess auth', $this->root);

								$ret['result'] = 'success';

							} else $ret['result'] = 'failed';

						} else $ret['result'] = 'failed';

					} else {

						$randhash = randHash();
						$ret['chal'] = $randhash;
						$ret['result'] = 'challange';

						//store challange
						$sess['chal'] = $randhash;
						$this->db->putObject_($sess, 'sess chal', $this->root);

					}

				} else $ret['result'] = 'this user is nologin';
				
			} else $ret['result'] = 'specify login';

		} else {
			$ret['result'] = 'only guest session can login. Just create new session.';
		}

		return $ret;*/
		
		$ret['result'] = 'unavailable';
		return $ret;
	}

	private function logout($inp, $user, $sess){

		if( isset($user['userinfo']['type']) ){
// 		if( $user['uid'] != 'guest' ){

			$sess['uid'] = 'guest';
			$this->db->putObject_($sess, 'sess unauth'/*, $this->root*/);

			$ret['result'] = 'success';

		} else {
			$ret['result'] = 'guest session can\'t logout';
		}

		return $ret;
	}
	
	private function external($inp, $user, $sess){

		if( isset($inp['obj']['module']) && isset($inp['obj']['func']) ){
		
			$module = $inp['obj']['module'];
			$func = $inp['obj']['func'];
			
			if( $module != "" && $func != "" ){
			
// 		 		$ext = new DvExternal();
		
// 				if( method_exists( $ext, $func ) ){
 				if( method_exists( $module, $func ) ){
 					$m = new $module();
					return $m->$func($inp, $user, $sess, $this->db);
				}
					
				$ret['result'] = 'there is no such module: '.$module.' or function :'.$func;
				return $ret;
			
			} else {

				$ret['result'] = 'module and function must be not empty';
				return $ret;
				
			}
		
		} else {
			
			$ret['result'] = 'specify module and func (function)';
			return $ret;
			
		}
			
	}

	// 	public abstract [result, challange] sendKey (String sid);

	// 	public abstract [result] logout (String sid);

	//	public [result] usermod();


}

?>
