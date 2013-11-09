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

class DvDBFiles implements DvDB {

	private $db;
	
	public function __construct($dbfilesdir = false){
		
		if( $dbfilesdir === false ){
			global $config;
			$dbfilesdir = $config['dbfilesdir'];
		}
		
		$this->db = new DataBase( $dbfilesdir );
		$this->initDB();
	}
	
	private function initDB(){
		if( !$this->db->isInitialized() ){
			$dbinit = new DataBaseInit();
			$dbinit->init($this->db);
		}
	}
	
	public function getDB(){
		return $this->db;
	}
	
	public function getSession($sid){
		return $this->db->getObjectByVal('sid', $sid, 'sess');
	}
	
	public function saveSession($sess){
		if( isset($sess['authed']) )
		if( $sess['authed'] == '1' ) $this->db->putObject($sess, 'sess auth');
		else $this->db->putObject($sess, 'sess unauth');
	}
	
	public function getUser($uid){
		return $this->db->getObjectByTags('user '.$uid);
	}
	
	public function saveUser($user){
		$this->db->putObject($user, 'user '.$user['uid']);
	}

	public function getSessionUserData($sid){
		return $this->getSession($sid);
	}

	public function getUserData($uid){
		return $this->getUser($uid);
	}
	
}

?>
