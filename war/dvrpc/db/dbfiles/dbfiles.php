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

/*
	Rights is 
	$rights['uid'] - owner id
	$rights['gid'] - owner group id
	$rights['ur'] - user can read: 1 or 0
	$rights['uw'] - user write
	$rights['gr'] - group read
	$rights['gw'] - group write
	$rights['or'] - owner read
	$rights['ow'] - owner write
	
	$user['uid'] - user id
	$user['gids'] - array: groups ids 
*/

abstract class DataBaseFiles {

	public function __construct() {
	}
	
	protected abstract getNewId();
	protected abstract store($obj)
	protected abstract deleteById($id);
	protected abstract readById($id);
	protected abstract readByTags($tags); // $tags: array
	
	public function DBObject getObjectById ($id, $user) {
		$obj = readById($id);
		$rights = $obj['rights'];
		
		if( $rights['uid'] == $user['uid'] ) return $obj['dbo'];
		else if( in_array($rights['gid'], $user['gids']) && ($rights['gr'] == '1') ) return $obj['dbo'];
		else if( $rights['or'] == '1' ) return $obj['dbo'];
		else return false;
	}

	public DBOBject getObjectByTags ($tags, $user) {
		$objs = readByTags()
	}

	public DBObject[] getObjectsByTags (long id, String[] tags, Rights rights, long count, long offset) {
		
	}

	/** returns id of added object */
	public function putObject ($dbo, $rights){
		
		check if user can write and update if exists
		
		$id = $this->getNewId();
		$obj['id'] = $id;
		$obj['dbo'] = $dbo;
		$obj['rights'] = $rights;
		store($obj);
		return $id;
	}

	public function delObject ($id) {
		$this->deleteById($id);
	}
}

?>
