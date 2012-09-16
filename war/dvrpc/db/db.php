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
require_once 'dbfilesdrv.php';

class DataBase {

	private $drv;
	private $ipc;

	public function __construct($dir){
		$this->drv = new DataBaseFilesDriver($dir);
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

			return $obj['dbo'];
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
	}

	/** returns id of added object
	 $tags: string, ' ' - separator */
	public function putObjects ($dbos, $tags/*, $user = null, $rights = null*/){

		$out = array();
		
		foreach( $dbos as $dbo ){

			$dbo['id'] = $this->putObject($dbo, $tags/*, $user, $rights*/);
			$out []= $dbo;			

		}
		
		return $out;
	}

	/** this function doesn't emit event
	 returns id of added object
	 $tags: string, ' ' - separator, if tags is false then tags will not be modified */
	public function putObject ($dbo, $tags = false/*, $user = null, $rights = null*/){

		if( isset($dbo['id']) ){

			$id = $dbo['id'];
			$obj = $this->drv->readById($id);
			if( $tags === false ) $tagsarr = false;
			else $tagsarr = explode(' ',$tags);
			$obj['id'] = $id;
			$obj['dbo'] = $dbo;
			$this->drv->store($id, $obj, $tagsarr);

			return $id;

		} else {

			//TODO check if user can write to specified tags

			$tagsarr = explode(' ',$tags);
			$id = $this->drv->getNewId();
			$obj['id'] = $id;
			$obj['dbo'] = $dbo;
			$this->drv->store($id, $obj, $tagsarr);

			return $id;

		}
	}

	public function delObjects ($ids) {
		foreach( $ids as $id ){
			$this->delObject($id);
		}
	}

	public function delObject ($id) {
		if( is_numeric($id) ){

			$obj = $this->drv->readById($id);
			if( $obj === false ) return true;
			$this->drv->deleteById($id);
			return true;
			
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
