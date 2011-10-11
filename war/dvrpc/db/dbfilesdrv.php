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

require_once 'dbdrv.php';

class DataBaseFilesDriver implements DataBaseDriver {

	private $dir;
	private $lastidfile;
	private $tagsfile;//hashmap tag => id1. id2. id3
	
	public function __construct($dir){
		$this->dir = $dir;
		$this->lastidfile = $this->dir.'/lastid';
		$this->tagsfile = $this->dir.'/tags';
		
		if( file_exists($this->lastidfile) === false ){
			file_put_contents($this->lastidfile, serialize(1));
		}
		
		if( file_exists($this->tagsfile) === false ){
			file_put_contents($this->tagsfile, serialize(array()));
		}
		
	}
	
	public function getNewId(){
		$content = file_get_contents($this->lastidfile);
		$id = unserialize($content);
		$id++;
		file_put_contents($this->lastidfile, serialize($id));
		return $id;
	}
	
	private function getTags(){
		return unserialize(file_get_contents($this->tagsfile));
	}
	
	private function updateTags($tags, $id){
		//hashmap: tag => id1, id2, id3
		$alltags = $this->getTags();
		
		//tags of object with id == $id
		//contents: array of tags
		$objtagsfile = $this->dir.'/'."$id.tags";
		if( file_exists($objtagsfile) ){

			$oldtags = unserialize(file_get_contents($objtagsfile));
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
		
		if( count($tags) > 0 ) file_put_contents($objtagsfile, serialize($tags));
		else unlink($objtagsfile);
		file_put_contents($this->tagsfile, serialize($alltags));
		
	}
	
// 	private function removeTags($id){
// 		$alltags = $this->getTags();
// 		foreach( $alltags as $tag ){
// 			unset($tag[array_search($id, $tag)]);
// 		}
// 		file_put_contents($this->tagsfile, serialize($alltags));
// 	}
	
	/**
	 * write object to database without tags
	 */
	public function write($id, $obj){
		file_put_contents($this->dir.'/'.$id, serialize($obj));
	}
	
	//$tags - array
	public function store($id, $obj, $tags){
		$this->updateTags($tags, $id);
		file_put_contents($this->dir.'/'.$id, serialize($obj));
	}
	
	public function deleteById($id){
		$this->updateTags(array(), $id);
		unlink($this->dir.'/'.$id);
	}
	
	public function readById($id){
		$f = $this->dir.'/'.$id;
		if( file_exists($f) ){
			return unserialize(file_get_contents($f));
		} else return false;
	}
	
	// $tags: array
	public function readByTags($tags, $count = 0, $offset = 0){
		$alltags = $this->getTags();
		$tags = array_intersect($tags, array_keys($alltags));
		if( count($tags) > 0 ){
			
			$ids = $alltags[array_shift($tags)];
		
	// 		print "tags: ".serialize($tags)."\n";
	// 		print "ids: ".serialize($ids)."\n";

			foreach($tags as $tag){
				$ids = array_intersect($ids, $alltags[$tag]);
			}
			if( $offset > count($ids) ) return array();
			
			if( $count != 0 && $offset != 0 ) $ids = array_splice ( $ids , $offset , $count );
			else if( $count != 0 ) $ids = array_splice ( $ids , 0 , $count );
			else if( $offset != 0 ) $ids = array_splice ( $ids , $offset );
				
			$ret = array();
			foreach( $ids as $id ){
				$obj = $this->readById($id);
				$obj['dbo']['id'] = $id;
				$ret[] = $obj;
			}
			return $ret;
		
		} else return array();
	}
	
}

?>
