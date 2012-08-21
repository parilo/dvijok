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

require_once "dvipc.php";
require_once 'dbfilesdrv.php';

class DataBase {

	private $drv;
// 	private $defuser;
// 	private $defrights;
	private $ipc;

	public function __construct($dir){
		$this->drv = new DataBaseFilesDriver($dir);

// 		$this->defuser['uid'] = 'root';
// 		$this->defuser['gids'][] = 'root';

// 		$this->defrights['uid'] = 'root';
// 		$this->defrights['gid'] = 'root';
// 		$this->defrights['ur'] = '1';
// 		$this->defrights['uw'] = '1';
// 		$this->defrights['gr'] = '0';
// 		$this->defrights['gw'] = '0';
// 		$this->defrights['or'] = '0';
// 		$this->defrights['ow'] = '0';

		$this->ipc = new DVIPCSys();
	}

	public function isInitialized() {
		$ret = $this->drv->readById('isinit');
		if( $ret === false ) return false;
		else return true;
	}

	public function setInitialized() {
		$this->drv->write('isinit', 1);
	}
	
	public function getObjectsByIds($ids/*, $user*/){
		$objs = array();
		foreach( $ids as $id ){
			$obj = $this->getObjectById($id/*, $user*/);
			if( $obj != false ) $objs []= $obj; 
		}
		return $objs;
	}

	public function getObjectById ($id/*, $user*/) {
		if( is_numeric($id) ){
			$obj = $this->drv->readById($id);
			$obj['dbo']['id'] = $id;
// 			$rights = $obj['rights'];

			return $obj['dbo'];
// 			if( $rights['uid'] == $user['uid'] ) return $obj['dbo'];
// 			else if( ($rights['gr'] == '1') && in_array($rights['gid'], $user['gids']) ) return $obj['dbo'];
// 			else if( $rights['or'] == '1' ) return $obj['dbo'];
// 			else return false;
		} else return false;
	}

	public function getObjectByTags ($tags/*, $user*/) {
		$objs = $this->getObjectsByTags($tags,/* $user,*/ 1, 0);
		if( count($objs) > 0 ) return $objs[0];
		else return false;
	}

	//$tags: string, ' ' - separator
	public function getObjectsByTags ($tags, /*$user,*/ $count = 0, $offset = 0) {
 		return $this->drv->readByTags(explode(' ',$tags), $count, $offset);
// 		$objs = $this->drv->readByTags(explode(' ',$tags), $count, $offset);
		// 		print "objs: ".serialize($objs)."\n";
// 		$ret = array();
// 		$uid = $user['uid'];
// 		$gids = $user['gids'];
// 		foreach($objs as $obj){
// 			$rights = $obj['rights'];

// 			if( $rights['uid'] == $uid ) $ret[] = $obj['dbo'];
// 			else if( ($rights['gr'] == '1') && in_array($rights['gid'], $gids) ) $ret[] = $obj['dbo'];
// 			else if( $rights['or'] == '1' ) $ret[] = $obj['dbo'];
// 		}
// 		return $ret;
	}

	/** returns id of added object
	 $tags: string, ' ' - separator */
	public function putObjects ($dbos, $tags/*, $user = null, $rights = null*/){
		
// 		if( count($dbos) > 0 ){
// 			$dbo = $dbos[0];
// 			if( isset($dbo['id']) ){
// 				//array
// 				$oldtags = $this->getTagsById($dbo['id']);
// 			}
// 		}
		
// 		$added = array();
// 		$moded = array();

		$out = array();
		
		foreach( $dbos as $dbo ){

			$dbo['id'] = $this->putObject_($dbo, $tags/*, $user, $rights*/);
			$out []= $dbo;			
// 			if( isset($dbo['id']) ){

// 				if( $id !== false )	$moded[] = $dbo;
					
// 			} else {
// 				$dbo['id'] = $id;
// 				$added[] = $dbo;
// 			}

		}
		
		return $out;

// 		//add event
// 		if( count($added) ){

// 			$added['_isarr'] = '1';

// 			$event['type'] = 'add';
// 			$event['objs'] = $added;
// 			$event['tags'] = $tags;
// 			$this->ipc->invokeEvent($event);

// 		}

// 		//mod event
// 		if( count($moded) ){

// 			$moded['_isarr'] = '1';

// 			$event['type'] = 'mod';
// 			$event['objs'] = $moded;
// 			$event['tags'] = implode(" ", array_unique(array_merge($oldtags, explode(" ", $tags))));
// 			$oldtags['_isarr'] = '_isarr';
// 			$newtags = explode(" ", $tags);
// 			$newtags['_isarr'] = '_isarr';
// 			$event['oldtags'] = $oldtags;
// 			$event['newtags'] = $newtags;
// 			$this->ipc->invokeEvent($event);

// 		}

	}

	/** returns id of added object
	 $tags: string, ' ' - separator */
	public function putObject ($dbo, $tags/*, $user = null, $rights = null*/){

// 		if( isset($dbo['id']) ){
// 			//array
// 			$oldtags = $this->getTagsById($dbo['id']);
// 		}
		
		$id = $this->putObject_($dbo, $tags/*, $user, $rights*/);

// 		if( isset($dbo['id']) ){

// 			if( $id !== false ){
// 				$event['type'] = 'mod';
// 				$event['obj'] = $dbo;
// 				//mixed old and new tags
// 				$event['tags'] = implode(" ", array_unique(array_merge($oldtags, explode(" ", $tags))));
// 				$oldtags['_isarr'] = '_isarr';
// 				$newtags = explode(" ", $tags);
// 				$newtags['_isarr'] = '_isarr';
// 				$event['oldtags'] = $oldtags;
// 				$event['newtags'] = $newtags;
// 				$this->ipc->invokeEvent($event);
// 			}

// 		} else {

// 			$dbo['id'] = $id;
// 			$event['type'] = 'add';
// 			$event['obj'] = $dbo;
// 			$event['tags'] = $tags;
// 			$this->ipc->invokeEvent($event);

// 		}

		return $id;

	}

	/** this function doesn't emit event
	 returns id of added object
	 $tags: string, ' ' - separator, if tags is false then tags will not be modified */
	public function putObject_ ($dbo, $tags = false/*, $user = null, $rights = null*/){

// 		$defrights = false;
// 		if( $rights == null ){
// 			$rights = $this->defrights;
// 			$defrights = true;
// 		}
// 		if( $user == null ) $user = $this->defuser;

		if( isset($dbo['id']) ){

			$id = $dbo['id'];
			$obj = $this->drv->readById($id);
// 			$uid = $user['uid'];
// 			$gids = $user['gids'];
// 			$oldrights = $obj['rights'];
// 			if(
// 			( $oldrights['uid'] == $user['uid'] ) ||
// 			( ($oldrights['gw'] == '1') && in_array($oldrights['gid'], $gids) ) ||
// 			( $oldrights['ow'] == '1' )
// 			){
				if( $tags === false ) $tagsarr = false;
				else $tagsarr = explode(' ',$tags);
				$obj['id'] = $id;
				$obj['dbo'] = $dbo;
// 				if( $defrights === true) $obj['rights'] = $oldrights;
// 				else $obj['rights'] = $rights;
				$this->drv->store($id, $obj, $tagsarr);

				return $id;
// 			} else return false;

		} else {

			//TODO check if user can write to specified tags

			$tagsarr = explode(' ',$tags);
			$id = $this->drv->getNewId();
			$obj['id'] = $id;
			$obj['dbo'] = $dbo;
// 			$obj['rights'] = $rights;
			$this->drv->store($id, $obj, $tagsarr);

			return $id;

		}
	}

	public function delObjects ($ids/*, $user*/) {
// 		$deled = array();
// 		$tags = array();
		foreach( $ids as $id ){
				
// 			$todeltags = $this->drv->readTagsById($id);
// 			$ret = $this->delObject_($id, $user);
			$this->delObject_($id/*, $user*/);
// 			if( $ret === true ){
// 				$deled[] = $id;
// 				$tags = array_unique(array_merge( $tags, $todeltags));
// 			}
				
		}

// 		if( count($deled) > 0 ){
				
// 			$deled['_isarr'] = '1';
				
// 			$event['type'] = 'del';
// 			$event['objs'] = $deled;
// 			$event['tags'] = implode(' ', $tags);
// 			$this->ipc->invokeEvent($event);
// 		}
	}

	public function delObject ($id/*, $user*/) {
		if( is_numeric($id) ){

// 			$event['type'] = 'del';
// 			$event['tags'] = implode(' ', $this->drv->readTagsById($id));
// 			$event['id'] = $id;

			$ret = $this->delObject_($id/*, $user*/);
// 			if( $ret === true ) $this->ipc->invokeEvent($event);
			return $ret;
				
		} else return "id must be numeric";
	}

	// this function doesn't emit event
	public function delObject_ ($id/*, $user*/) {
		if( is_numeric($id) ){

			$obj = $this->drv->readById($id);
			if( $obj === false ) return true;

// 			$uid = $user['uid'];
// 			$gids = $user['gids'];
// 			$rights = $obj['rights'];
// 			if(
// 			( $rights['uid'] == $user['uid'] ) ||
// 			( ($rights['gw'] == '1') && in_array($rights['gid'], $gids) ) ||
// 			( $rights['ow'] == '1' )
// 			){
				$this->drv->deleteById($id);
				return true;
// 			} else return "deletion failed";
		} else return "id must be numeric";
	}

	/**
	 $tags: string, ' ' - separator */
// 	public function putTags($tags, $tagsrights) {
// 		$tagsarr = explode(' ',$tags);
// 		$tr = $this->drv->readById('tagsrights');
// 		foreach($tagsarr as $tag){
// 			$tr[$tag] = $tagsrights;
// 		}
// 		$this->drv->write('tagsrights', $tr);
// 	}

	public function getObjectByVal($key, $val, $tags/*, $user*/){
		$objs = $this->getObjectsByTags($tags/*, $user*/);
		foreach( $objs as $obj ){
			if( isset($obj[$key]) ) if( $obj[$key] == $val ) return $obj;
		}
		return false;
	}
	
	public function getObjectsByVal($key, $val, $tags){
		$objs = $this->getObjectsByTags($tags);
		$ret = array();
		foreach( $objs as $obj ){
			if( isset($obj[$key]) ) if( $obj[$key] == $val ) $ret []= $obj;
		}
		return $ret;
	}
	
	public function getTagsById($id){
		if( is_numeric($id) ){
			return $this->drv->readTagsById($id);
		} else return false;
	}
}

?>
