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
require_once '../config.php';

class DataBaseFilesDriver implements DataBaseDriver {

	private $dir;
	private $lastidfile;
	private $tagsfile;
	
	public function __construct(){
		global $config;
		$this->dir = $config['dbfilesdir'];
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
	
	private function addTags($tags, $id){
		print "dvfilesdrv.php: addTags: need to add only not present tags and remove that was removed\n";
		$alltags = $this->getTags();
		foreach( $tags as $tag ){
			$alltags[$tag][] = $id;
		}
		file_put_contents($this->tagsfile, serialize($alltags));
	}
	
	private function removeTags($id){
		$alltags = $this->getTags();
		foreach( $alltags as $tag ){
			unset($tag[array_search($id, $tag)]);
		}
		file_put_contents($this->tagsfile, serialize($alltags));
	}
	
	//$tags - array
	public function store($id, $obj, $tags){
		$this->addTags($tags, $id);
		file_put_contents($this->dir.'/'.$id, serialize($obj));
	}
	
	public function deleteById($id){
		$this->removeTags($id);
		unlink($this->dir.'/'.$id);
	}
	
	public function readById($id){
		$f = $this->dir.'/'.$id;
		if( file_exists($f) ){
			return unserialize(file_get_contents($f));
		} else return false;
	}
	
	// $tags: array
	public function readByTags($tags, $count, $offset){
		$alltags = $this->getTags();
		$ids = $alltags[array_shift($tags)];
		foreach($tags as $tag){
			$ids = array_intersect($ids, $alltags[$tag]);
		}
		if( count($ids) > $offset ) return array();
		$ids = array_splice ( $ids , $offset , $count );
		$ret = array();
		foreach( $ids as $id ){
			$ret[] = $this->readById($id);
		}
		return $ret;
	}
	
}

?>
