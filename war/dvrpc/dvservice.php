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
require_once 'dvobjfilter.php';

class DVService {

	private $db;
	private $expTimeAnon;

	public function __construct(){
		$this->initExpTime();
		$this->initDB();
	}

	private function initExpTime(){
		global $config;
		$this->expTimeAnon = $config['SESSION_EXPIRATION_TIME_ANON'];
	}

	private function initDB(){
		global $config;
		if( $config['dbtype'] == 'dbfiles' ){
			require_once 'dvdbfiles.php';
			$this->db = new DvDBFiles();
		} else if( $config['dbtype'] == 'mysql' ){
			require_once 'dvdbmysql.php';
			$this->db = new DvDBMysql();
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
			
// 				$sess = $this->db->getObjectByVal('sid', $sid, 'sess'/*, $this->root*/);
				$sess = $this->db->getSession($sid);
				if( $sess === false ) $ifsess = false;
				else {
					//TODO pick correct expiration time for guest and authorized users
					$expTime = $this->expTimeAnon;
					$now = nowMinuts();
					if( ($sess['time'] + $expTime) < $now ) $ifsess = false;
					else {
						$sess['time'] = $now;
// 						$this->db->putObject_($sess, false/*, $this->root*/);
						$this->db->saveSession($sess);
						$ifsess = true;
					}
	
				}
			
			} else $ifsess = false;
			
		} else $ifsess = false;

		if( $ifsess === false  ){
			$ret['result'] = 'notsid';
			return $ret;
		} else {

// 			$user = $this->getUser($sess['uid']);
			$user = $this->db->getUser($sess['uid']);
				
			if( $user === false ) return retarr('notsid'); //return retarr('notuser');
			else {
				$user['ip'] = $ip;
				return $this->$method($obj, $user, $sess);
			}
		}
	}

	private function checkSessionFunc($inp, $user, $sess){
 		
 		if( $sess['uid'] == 'guest' ){
			$userdata = $this->db->getSessionUserData($sess['sid']);
 		} else {
			$userdata = $this->db->getUserData($sess['uid']);
# 			$ret['objs']['userdata'] = isset($userdata['userdata'])?$userdata['userdata']:array();
# 			$ret['objs']['userinfo'] = isset($userdata['userinfo'])?$userdata['userinfo']:array();
 		}
		$ret['objs']['userdata'] = isset($userdata['userdata'])?$userdata['userdata']:array();
 		$ret['objs']['userinfo'] = isset($userdata['userinfo'])?$userdata['userinfo']:array();
 		
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
		$sess['authed'] = '0';
// 		$sess['userinfo'] = array();

// 		$this->db->putObject_($sess, 'sess unauth'/*, $this->root*/);
		$this->db->saveSession($sess);
		$retobj['sid'] = $sid;

		$ret['result'] = "success";
		$ret['objs'] = $retobj;
		return $ret;
	}

	private function saveUserData($inp, $user, $sess){
		
 		if( $sess['uid'] == 'guest' ){
 			$sess['userdata'] = $inp['userdata'];
//  			$this->db->putObject_($sess);
			$this->db->saveSession($sess);
 		} else {
 			$user['userdata'] = $inp['userdata'];
//  			$this->db->putObject_($user);
 			$this->db->saveUser($user);
 		}

 		$ret['result'] = "success";
 		return $ret;
	}

	private function login($inp, $user, $sess){
		// need rewriting
		$inp = $inp['obj'];

		if( $user['uid'] == 'guest' ){

			if( isset($inp['login']) ){
					
				$login = $inp['login'];
// 				$loginuser = $this->db->getObjectByVal('uid', $login, 'user', $this->root);
				$loginuser = $this->db->getUser($login);
				
				if( ($loginuser != false) && !isset($loginuser['nologin']) ){

					if( isset($inp['response']) ){

						$resp = strtolower($inp['response']);
						$sessuserdata = $this->db->getSessionUserData($sess['sid']);
						
						if( isset($sessuserdata['serverdata']['chal']) && isset($loginuser['pass']) ){
														
							if( $resp === md5($sessuserdata['serverdata']['chal'].$loginuser['pass']) ){

								unset($sessuserdata['serverdata']['chal']);
								$sess['data'] = $sessuserdata;
								$sess['uid'] = $loginuser['uid'];
								//$this->db->putObject_($sess, 'sess auth', $this->root);
								$sess['authed'] = '1';
								$this->db->saveSession($sess);
								
								$userdata = $this->db->getUserData($sess['uid']);

								$ret['objs']['userinfo'] = isset($userdata['userinfo'])?$userdata['userinfo']:array();
								$ret['objs']['userdata'] = isset($userdata['userdata'])?$userdata['userdata']:array();
								$ret['result'] = 'success';

							} else $ret['result'] = 'failed';

						} else $ret['result'] = 'failed';

					} else {

						$randhash = randHash();
						$ret['chal'] = $randhash;
						$ret['result'] = 'challange';

						//store challange
						$sess['serverdata']['chal'] = $randhash;
						$this->db->saveSession($sess);
// 						$this->db->putObject_($sess, 'sess chal', $this->root);

					}

				} else $ret['result'] = 'this user is nologin';
				
			} else $ret['result'] = 'specify login';

		} else {
			$ret['result'] = 'only guest session can login. Just create new session.';
		}

		return $ret;
		
// 		$ret['result'] = 'unavailable';
// 		return $ret;
	}

	private function logout($inp, $user, $sess){

		if( isset($user['userinfo']['type']) ){
// 		if( $user['uid'] != 'guest' ){

			$sess['uid'] = 'guest';
			$sess['authed'] = '0';
// 			$this->db->putObject_($sess, 'sess unauth'/*, $this->root*/);
			$this->db->saveSession($sess);

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
