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

require_once 'lib.php';
require_once 'dbdrv.php';
require_once 'dbexception.php';

class DataBaseFilesDriver implements DataBaseDriver {

	private $dir;
	private $lastidfile;
	private $tagsfile;//hashmap tag => id1. id2. id3

	public function __construct($dir){
		$this->dir = $dir;
		$this->lastidfile = $this->dir.'/lastid';
		$this->tagsfile = $this->dir.'/tags';

		if( file_exists($this->lastidfile) === false ){
			if( fileWrite($this->lastidfile, serialize(0)) === false ) throw new DBException();
		}

		if( file_exists($this->tagsfile) === false ){
			if( fileWrite($this->tagsfile, serialize(array()), LOCK_EX) === false ) throw new DBException();
		}

	}

	public function getNewId(){
		$content = fileRead($this->lastidfile);
		if( $content === false ) throw new DBException();
		$id = unserialize($content);
		if( $id === false ) throw new DBException();
		$id++;
		if( fileWrite($this->lastidfile, serialize($id)) === false ) throw new DBException();
		return $id;
	}

	private function getTags(){
		$res = fileRead($this->tagsfile);
		if( $res === false ) throw new DBException();
// 		error_log(strlen($res).': '.$res);
		$res = unserialize($res);
		if( $res === false ) throw new DBException();
		return $res;
	}

	private function updateTags($tags, $id){
		//hashmap: tag => id1, id2, id3
		$alltags = $this->getTags();

		//tags of object with id == $id
		//contents: array of tags
		$objtagsfile = $this->dir.'/'."$id.tags";
		if( file_exists($objtagsfile) ){

			$cont = fileRead($objtagsfile); if( $cont === false ) throw new DBException();
			$oldtags = unserialize($cont);
			if( $oldtags === false ) throw new DBException();
			$toremove = array_diff($oldtags, $tags);
			$toadd = array_diff($tags, $oldtags);
			foreach( $toremove as $tag ){
				// 				$ids = $alltags[$tag];
				unset($alltags[$tag][array_search($id, $alltags[$tag])]);
				if( count($alltags[$tag]) < 1 ) unset($alltags[$tag]);
			}
				
		} else {
			$toadd = $tags;
		}

		foreach( $toadd as $tag ){
			$alltags[$tag][] = $id;
		}

		if( count($tags) > 0 ){
			if( fileWrite($objtagsfile, serialize($tags)) === false ) throw new DBException();
		} else {
			if( unlink($objtagsfile) === false ) throw new DBException();
		}
		if( fileWrite($this->tagsfile, serialize($alltags)) === false ) throw new DBException();

	}

	// 	private function removeTags($id){
	// 		$alltags = $this->getTags();
	// 		foreach( $alltags as $tag ){
	// 			unset($tag[array_search($id, $tag)]);
	// 		}
	// 		!!!file_put_contents($this->tagsfile, serialize($alltags));
	// 	}

	/**
	 * write object to database without tags
	 */
	public function write($id, $obj){
		if( fileWrite($this->dir.'/'.$id, serialize($obj)) === false ) throw new DBException();
	}

	//$tags - array
	public function store($id, $obj, $tags){
		if( $tags !== false ) $this->updateTags($tags, $id);
		if( fileWrite($this->dir.'/'.$id, serialize($obj)) === false ) throw new DBException();
	}

	public function deleteById($id){
		$this->updateTags(array(), $id);
		if( unlink($this->dir.'/'.$id) === false ) throw new DBException();
	}

	public function readById($id){
		return $this->readById2($id, 0);
	}

	public function readById2($id, $i){
		$f = $this->dir.'/'.$id;
		if( file_exists($f) ){
			$cont = fileRead($f);
			if( $cont === false ) {
				if( $i<10 ) { usleep(100000); return $this->readById2($id, $i+1); }
				else throw new DBException('cont: '.$cont);
			}
			$res = unserialize($cont);
			if( $res === false ) {
				if( $i<10 ) { usleep(100000); return $this->readById2($id, $i+1); }
				else {
					unlink($f);
					throw new DBException('res: '.print_r($res));
				}
			}
			return $res;
		} else return false;
	}
	
	public function readTagsById($id){
		$f = $this->dir.'/'.$id.'.tags';
		if( file_exists($f) ){
			$cont = fileRead($f);
			if( $cont === false ) throw new DBException();
			$res = unserialize($cont);
			if( $res === false ) throw new DBException();
			return $res;
		} else return false;
	}
	
	// $tags: array
	public function readByTags($tags, $count = 0, $offset = 0){
		// 		$tags = array_intersect($tags, array_keys($alltags));

		$alltags = $this->getTags();
		foreach($tags as $tag){
			if( !isset($alltags[$tag]) ) return array();
		}

		if( count($tags) > 0 ){
				
			$ids = $alltags[array_shift($tags)];

			// 			print "tags: ".serialize($tags)."\n";
			// 			print "ids: ".serialize($ids)."\n";

			foreach($tags as $tag){
				$ids = array_intersect($ids, $alltags[$tag]);
			}
			if( $offset > count($ids) ) return array();
				
			if( $count != 0 && $offset != 0 ) $ids = array_splice ( $ids , $offset , $count );
			else if( $count != 0 ) $ids = array_splice ( $ids , 0 , $count );
			else if( $offset != 0 ) $ids = array_splice ( $ids , $offset );

// 			$ret = array();
// 			foreach( $ids as $id ){
// 				$obj = $this->readById($id);
// 				$obj['dbo']['id'] = "$id";
// 				$ret[] = $obj;
// 			}

			$ret = array();
			foreach( $ids as $id ){
				$obj = $this->readById($id);
				$obj['dbo']['id'] = "$id";
				$ret[] = $obj['dbo'];
			}
			return $ret;
				
		} else return array();
	}

}

?>
