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


class DvUser {

	public static function ismoderator($user){
		$ismod = isset($user['userinfo']['ismoderator']);
		if( $ismod ) $ismod = ($user['userinfo']['ismoderator'] == '1');
		return $ismod;
	}
	
	public static function isauthorized($user){
		return isset($user['uid'])?$user['uid'] != 'guest':false;
	}
	
	public static function getUser($db, $uid){
		return $db->getObjectByTags('user '.$uid);
	}
	
}

?>
