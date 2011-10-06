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
	private $root;

	public function __construct(){
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
		} else if( $func == "login" ){
			return $this->login($obj);
		} else if( $func == "listenForEvent" ){
			return $this->listenForEvent();
		} else if( $func == "testIPC" ){
			return $this->testIPC();
		}
		
		$ret['result'] = 'function not found';
		return $ret;
		
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
 		$sess['exp'] = floor(time()/60) + $this->expTimeAnon;
 		$sess['ip'] = $ip;
 		
 		$this->db->putObject($sess, 'sess unauth');
 		$retobj['sid'] = $sid;
 		
		$ret['result'] = "success";
		$ret['objs'] = $retobj;
		return $ret;
	}
	
	private function login($obj){
		if( isset($obj['sid']) ){
			$sid = $obj['sid'];
			$sess = $this->db->getObjectByVal('sid', $sid, 'sess unauth', $this->root);
			a
		} else {
			$retobj['result'] = 'sid is required';
		}
	}
	
// 	public abstract [result, challange] login (String sid);
	
// 	public [result] authResponse (String sid, String response) 
	
// 	public abstract [result, challange] sendKey (String sid);
	
// 	public abstract [result] logout (String sid);

//	public [result] usermod();
	
// 	public abstract [result, DBObject] getObject (String sid, String[] tags);
	
// 	public abstract [result, DBObject] getObjectById (String sid, String id);
	
// 	public abstract [result, DBObject[]] getObjects (String sid, String[] tags, long count, long offset);
	
// 	public abstract [result] putObject (String sid, String[] tags, unix rights rights);
	
// 	public abstract [result] delObject (String sid, String id);
	
	
}

?>
