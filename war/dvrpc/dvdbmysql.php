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

require_once "db/db.php";
require_once "db/dbinit.php";
require_once "dvdb.php";

class DvDBMysql implements DvDB {

	private $db;
	
	public function __construct(){
		$this->initDB();
	}
	
	private function initDB(){
		global $config;
		
		$this->db = new mysqli($config['MYSQLHOST'], $config['MYSQLUSER'], $config['MYSQLPASS'], $config['MYSQLDBNAME']);
		
		if ($this->db->connect_error) {
			die('DB Connect Error (' . $this->db->connect_errno . ') '. $this->db->connect_error);
		}

		if (!$this->db->set_charset("utf8")) {
			die("Error loading character set utf8: ".$this->db->error."\n");
		}
		
		/*
		$result = $this->db->query("SELECT COUNT(id) AS count FROM dvuser");
		if (!$result) {
			die('initDB: Error (' . $this->db->errno . ') '. $this->db->error);
		}
		
		$row = $result->fetch_assoc();
		if( $row['count'] == 0 ){
			$guest['uid'] = 'guest';
			$this->saveUser($guest);
		}
		*/
	}
	
	public function getDB(){
		return $this->db;
	}
	
	public function getSession($sid){

		$result = $this->db->query('
				SELECT s.id, s.sid, s.uid, s.time, s.ip, s.authed
				FROM dvsession s
				WHERE s.sid=\''.$this->db->real_escape_string($sid).'\'');

		if (!$result) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
		if( is_object($result) ){
			
			$sess = $result->fetch_assoc();
// error_log("db sess: ".print_r($sess, true));
// 			if( $sess['data'] != '' ){
// 				$data = unserialize($sess['data']);
// 				unset($sess['data']);
// 				if(is_array($data))
// 				foreach( $data as $key => $val ){
// 					$sess[$key] = $val;
// 				}
// 			}
			
			return $sess;
		}
		else return false;
		
	}
	
	public function getSessionUserData($sid){
		
		$result = $this->db->query('
				SELECT s.data
				FROM dvsession s
				WHERE s.sid=\''.$this->db->real_escape_string($sid).'\'');
		
		if (!$result) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
		if( is_object($result) ){

			$sess = $result->fetch_assoc();
			if( $sess['data'] != '' ){
// 				$data = unserialize($sess['data']);
				$data = json_decode($sess['data'], true);
				unset($sess['data']);
				if(is_array($data)){
					$userdata = array();
					foreach( $data as $key => $val ){
						$userdata[$key] = $val;
					}
					return $userdata;
				}
			}
				
		}
		return array();
		
	}
	
	public function saveSession($sess){
		
		if( isset($sess['userdata']) ){
			$sess['data']['userdata'] = $sess['userdata'];
			unset($sess['userdata']);
		}
		
		if( isset($sess['userinfo']) ){
			$sess['data']['userinfo'] = $sess['userinfo'];
			unset($sess['userinfo']);
		}
		
		if( isset($sess['serverdata']) ){
			$sess['data']['serverdata'] = $sess['serverdata'];
			unset($sess['serverdata']);
		}
		
// 		if( isset($sess['data']) ) $sess['data'] = serialize($sess['data']);
		if( isset($sess['data']) ) $sess['data'] = json_encode($sess['data']);
		
		$sess = $this->quoteSession($sess);
		
		if( isset($sess['id']) ){
				
			$id = $sess['id'];
			unset($sess['id']);
			$upd = $this->formatUpdate($sess);
			$result = $this->db->query('UPDATE dvsession SET '.$upd.' WHERE id='.$id);
			if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
				
		} else {
				
			$ins = $this->formatInsert($sess);
			$result = $this->db->query('INSERT INTO dvsession ('.$ins['fields'].') VALUES ('.$ins['vals'].')');
			if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
				
		}
		
	}

// 	public function setSessionUnauth($sess){
// 		$this->setSessionAuth($sess, false);
// 	}
	
// 	public function setSessionAuth($sess, $ifauth){
// 		$authed = $ifauth?'1':'0';
// 		$result = $this->db->query("UPDATE dvsession SET authed=$authed WHERE sid=".$sess['sid']);
// 		if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
// 	}
	
	public function getUser($uid){
		
		$result = $this->db->query("
				SELECT id, uid, pass
				FROM dvuser
				WHERE uid='".$this->db->real_escape_string($uid).'\'');

		if (!$result) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
		if( is_object($result) ){
				
			$user = $result->fetch_assoc();
// 			if( $user['data'] != '' ){
// 				$data = unserialize($user['data']);
// 				unset($user['data']);
// 				if(is_array($data))
// 				foreach( $data as $key => $val ){
// 					$user[$key] = $val;
// 				}
// 			}
				
			return $user;
		}
		else return false;
		
	}
	
	public function getUserData($uid){
		
		$result = $this->db->query("
				SELECT *
				FROM dvuser
				WHERE uid='".$this->db->real_escape_string($uid).'\'');
		
		if (!$result) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
		if( is_object($result) ){
		
			$user = $result->fetch_assoc();
			if( $user == false ) return false;
			
			if( $user['data'] != '' ){
// 				$data = unserialize($user['data']);
				$data = json_decode($user['data'], true);
				unset($user['data']);
				if(is_array($data)){
					$userdata = array();
					foreach( $data as $key => $val ){
						$userdata[$key] = $val;
					}
				}
			}
			
			$fields = array('id', 'uid', 'pass', 'data');
			$ui = array(); 
			foreach( $user as $key => $val ){
				if( !in_array($key, $fields) ){
					$ui[$key] = $val;
				}
			}
			$userdata['userinfo'] = $ui;

			return $userdata;
				
		}
		return array();
		
	}
	
	public function saveUser($user){
		
		if( isset($user['userdata']) ){
			$user['data']['userdata'] = $user['userdata'];
			unset($user['userdata']);
		}
		
		if( isset($user['userinfo']) ){
// 			$user['data']['userinfo'] = $user['userinfo'];
			unset($user['userinfo']);
		}
		
// 		if( isset($user['data']) ) $user['data'] = serialize($user['data']);
 		if( isset($user['data']) ) $user['data'] = json_encode($user['data']);
		
		$user = $this->quoteUser($user);
		
 		if( isset($user['id']) )
		if( $this->userExistsById($user['id']) ){
		
			$id = $user['id'];
			unset($user['id']);
			$upd = $this->formatUpdate($user);
			$sql = 'UPDATE dvuser SET '.$upd.' WHERE id='.$id;
			$result = $this->db->query($sql);
			if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error.' sql: '.$sql);
			
		} else {
				
			$ins = $this->formatInsert($user);
			$result = $this->db->query('INSERT INTO dvuser ('.$ins['fields'].') VALUES ('.$ins['vals'].')');
			if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error);
			
		}
		
	}
	
	
	public function saveUserData($uid, $userinfo, $types){

		$uid = $this->db->real_escape_string($uid);
		
		$set = '';
		foreach( $userinfo as $name => $value ){
			$name = $this->db->real_escape_string($name);
			
			if( $types[$name] == 'str' ) $value = "'".$this->db->real_escape_string($value)."'";
			else if( $types[$name] == 'float' ) settype($value, 'float');
			else settype($value, 'integer');
			
			$set .= ', `'.$name.'` = '.$value;
		}
		$set = substr($set, 2);
		
		$sql = "UPDATE dvuser SET $set WHERE uid='$uid'";
		$result = $this->db->query($sql);
		if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error.' sql: '.$sql);
		
	}
	
	private function userExistsById($id){
		$sql = "SELECT count(*) count FROM dvuser WHERE id=$id";
		$result = $this->db->query($sql);
		if( !$result ) throw new DBException('mysql error (' . $this->db->errno . ') '. $this->db->error.' sql: '.$sql);
		$row = $result->fetch_assoc();
		if( $row['count'] > 0 ) return true;
		else return false;
	}
	
	private function quoteUser($user){
		if( isset($user['uid']) ) $user['uid'] = '\''.$this->db->real_escape_string($user['uid']).'\'';
		if( isset($user['pass']) ) $user['pass'] = '\''.$this->db->real_escape_string($user['pass']).'\'';
		if( isset($user['data']) ) $user['data'] = '\''.$this->db->real_escape_string($user['data']).'\'';
		return $user;
	}
	
	private function quoteSession($sess){
		if( isset($sess['sid']) ) $sess['sid'] = '\''.$this->db->real_escape_string($sess['sid']).'\'';
		if( isset($sess['uid']) ) $sess['uid'] = '\''.$this->db->real_escape_string($sess['uid']).'\'';
		if( isset($sess['ip']) ) $sess['ip'] = '\''.$this->db->real_escape_string($sess['ip']).'\'';
		if( isset($sess['data']) ) $sess['data'] = '\''.$this->db->real_escape_string($sess['data']).'\'';
		return $sess;
	}
	
	private function formatInsert($obj){
		$fields = '';
		$vals = '';
		foreach( $obj as $key => $val ){
			$fields .= ', '.$key;
			$vals .= ', '.$val;
		}
		$fields = substr($fields ,1);
		$vals = substr($vals, 1);
		return array( 'fields' => $fields, 'vals' => $vals );
	}
	
	private function formatUpdate($obj){
		$set = '';
		foreach( $obj as $key => $val ){
			$set .= ', '.$key.'='.$val;
		}
		$set = substr($set ,2);
		return $set;
	}

		
}

?>
