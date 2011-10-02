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

require_once 'dbfilesdrv.php';

class DataBase {

	private $drv;
	
	public function __construct(){
		$this->drv = new DataBaseFilesDriver();
	}
	
	public function getObjectById ($id, $user) {
		$obj = $this->drv->readById($id);
		$rights = $obj['rights'];
		
		if( $rights['uid'] == $user['uid'] ) return $obj['dbo'];
		else if( ($rights['gr'] == '1') && in_array($rights['gid'], $user['gids']) ) return $obj['dbo'];
		else if( $rights['or'] == '1' ) return $obj['dbo'];
		else return false;
	}

	public function getObjectByTags ($tags, $user) {
		return $this->drv->getObjectsByTags($tags, $user, 1, 0);
	}
	
	//$tags: string, ' ' - separator
	public function getObjectsByTags ($tags, $user, $count, $offset) {
		$objs = $this->drv->readByTags(split(' ',$tags), $count, $offset);
		$ret = array();
		$uid = $user['uid'];
		$gids = $user['gids'];
		foreach($objs as $obj){
			$rights = $obj['rights'];
			
			if( $rights['uid'] == $uid ) $ret[] = $obj['dbo'];
			else if( ($rights['gr'] == '1') && in_array($rights['gid'], $gids) ) $ret[] = $obj['dbo'];
			else if( $rights['or'] == '1' ) $ret[] = $obj['dbo'];
		}
		return $ret;
	}

	/** returns id of added object
	$tags: string, ' ' - separator */
	public function putObject ($dbo, $tags, $user, $rights){
		
		if( isset($dbo['id']) ){
			
			$obj = readById($id);
			$uid = $user['uid'];
			$gids = $user['gids'];
			$rights = $obj['rights'];
			if(
				( $rights['uid'] == $user['uid'] ) ||
				( ($rights['gw'] == '1') && in_array($rights['gid'], $user['gids']) ) ||
				( $rights['ow'] == '1' )
			){
				$tagsarr = split(' ',$tags);
				$id = $dbo['id'];
				$obj['id'] = $id;
				$obj['dbo'] = $dbo;
				$obj['rights'] = $rights;
				$this->drv->store($id, $obj, $tagsarr);
				return $id;
			}
				
		} else {

			$tagsarr = split(' ',$tags);
			$id = $this->drv->getNewId();
			$obj['id'] = $id;
			$obj['dbo'] = $dbo;
			$obj['rights'] = $rights;
			$this->drv->store($id, $obj, $tagsarr);
			return $id;
			
		}
	}

	public function delObject ($id) {
		$drv->deleteById($id);
	}
}

?>
