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

require_once 'config.php';
require_once "dvipcfiles.php";
require_once "lib.php";
require_once "db/db.php";
require_once "db/dbinit.php";

class DVService {
	
	private $db;
	private $root;
	private $expTimeAnon;

	public function __construct(){
		$this->initExpTime();
		$this->initDB();
		
		$this->root['uid'] = 'root';
		$this->root['gids'][] = 'root';
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
			
		} else if( $func == "putObject" ){
			return $this->checkSession($obj, $ip, 'putObject');
			
		} else if( $func == "getObjects" ){
			return $this->checkSession($obj, $ip, 'getObjects');
			
		} else if( $func == "delObject" ){
			return $this->checkSession($obj, $ip, 'delObject');
			
		} else if( $func == "listenForEvent" ){
			return $this->listenForEvent();
			
		} else if( $func == "testIPC" ){
			return $this->testIPC();
		}
		
		$ret['result'] = 'function not found';
		return $ret;
		
	}
	
	/**
	 * 
	 * Checks user session
	 * @param DBObject $obj
	 * @param String $ip
	 * @param Method $method - this method will be invoked as method($obj) if session is valid 
	 */
	private function checkSession($obj, $ip, $method){
		//TODO remove expired sessions
		
		if( isset($obj['sid']) ){
			$sid = strtolower($obj['sid']);
			$sess = $this->db->getObjectByVal('sid', $sid, 'sess unauth', $this->root);
			if( $sess === false ) $ifsess = false;
			else {
				//TODO pick correct expiration time for guest and authorized users
				$expTime = $this->expTimeAnon;
				$now = nowMinuts();
				if( ($sess['time'] + $expTime) < $now ) $ifsess = false;
				else {
					$sess['time'] = $now;
					$this->db->putObject($sess, 'sess unauth', $this->root);
					$ifsess = true;
				}
				
			}
		} else $ifsess = false;
		
		if( $ifsess === false  ){
			$ret['result'] = 'notsid';
			return $ret;
		} else {
			
			$user = $this->getUser($sess['uid']);
			if( $user === false ) return retarr('notuser');
			else return $this->$method($obj['obj'], $user);
		}
	}
	
	private function testIPC(){
		$ipc = new DVIPCFiles();
		$event = $ipc->invokeEvent("testIPC event :)");
	}
	
	private function listenForEvent(){
		
		$ipc = new DVIPCFiles();
		$event = $ipc->listenForEvent();
	
		$ret['event'] = $event;
		$ret['result'] = 'success';
		return $ret;
	}
	
	private function removeOutdatedSessions(){}
	
	private function initSession($obj, $ip){
		
		$sid = randHash();
		$sess['sid'] = $sid;
 		$sess['uid'] = 'guest';
 		$sess['time'] = nowMinuts();
 		$sess['ip'] = $ip;
 		
 		$this->db->putObject($sess, 'sess unauth', $this->root);
 		$retobj['sid'] = $sid;
 		
		$ret['result'] = "success";
		$ret['objs'] = $retobj;
		return $ret;
	}
	
	private function getUser($uid){
		return $this->db->getObjectByVal('uid', $uid, 'user', $this->root);
	}

	private function putObject($inp, $user){
		
		$uid = $user['uid'];
		if( isset($inp['dbo']) ) $obj = $inp['dbo'];
		else return retarr('specify DBObject');
		
		if( isset($inp['tags']) ) $tags = $inp['tags'];
		else return retarr('specify tags');
		
		if( isset($inp['rights']) ) $rights = $inp['rights'];
		else {
			$rights['uid'] = $uid;
			$rights['gid'] = $uid;
			$rights['ur'] = '1';
			$rights['uw'] = '1';
			$rights['gr'] = '0';
			$rights['gw'] = '0';
			$rights['or'] = '0';
			$rights['ow'] = '0';
		}
		
		$id = $this->db->putObject($obj, $tags, $user, $rights);
		
		$ret['result'] = 'success';
		$ret['objs']['id'] = "$id";
		return $ret;
	}
	
	private function getObjects($inp, $user){
		
		if( !isset($inp['tags']) ) return retarr('specify tags');
		$tags = $inp['tags'];
		if( $tags == "" ) return retarr('specify tags');
		
		if( isset($inp['count']) ) $count = $inp['count'];
		else $count = 0;
		
		if( isset($inp['offset']) ) $offset = $inp['offset'];
		else $offset = 0;
		
		$objs = $this->db->getObjectsByTags($tags, $user, $count, $offset);
		$objs['_isarr'] = '1';
		$ret['result'] = 'success';
		$ret['objs'] = $objs;
		
		return $ret;
	}
	
	private function delObject($inp, $user){
		
		if( isset($inp['id']) ) $id = $inp['id'];
		else return retarr('specify id');
		
		$res = $this->db->delObject($id, $user);
		if( $res === true ){
			$ret['return'] = 'success';
			$ret['id'] = $id;
			return $ret;
		}
		else return retarr($res);
		
	}
	
// 	private function login($obj){
// 		if( isset($obj['sid']) ){
// 			$sid = $obj['sid'];
// 			$sess = $this->db->getObjectByVal('sid', $sid, 'sess unauth', $this->root);
// 			a
// 		} else {
// 			$retobj['result'] = 'sid is required';
// 		}
// 	}
	
// 	public abstract [result, challange] login (String sid);
	
// 	public [result] authResponse (String sid, String response) 
	
// 	public abstract [result, challange] sendKey (String sid);
	
// 	public abstract [result] logout (String sid);

//	public [result] usermod();
	
	
}

?>
