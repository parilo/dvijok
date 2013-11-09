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

require_once "history/dvipcresponses.php";
require_once "lib.php";

class DVIPCFiles extends DVIPCResponses {

	private $dir;
	private $timeout;
	
	public function __construct($ipcid, $filesdir, $timeout){
		parent::__construct($ipcid, $timeout);
		$this->dir = $filesdir;
		$this->timeout = $timeout;
	}
	
	protected function newDVIPC($id){
		return new DVIPCFiles($id, $this->dir, $this->timeout);
	}
	
	private function readFromFile($filename){
		if( file_exists($filename) ){
// 			$content = file_get_contents($filename);
			$content = fileRead($filename);
			return unserialize($content);
		} else return false;			
	}
	
	private function writeToFile($filename, $val){
 		$serval = serialize($val);
// 		file_put_contents($filename, $serval);
		fileWrite($filename, $serval);
	}
	
	protected function getEnvFromBus($name){
		$fname = $this->dir.'/'.$name.'_env';
		return $this->readFromFile($fname);
	}
	
	protected function putEnvToBus($name, $val){
		$fname = $this->dir.'/'.$name.'_env';
		$this->writeToFile($fname, $val);
	}
	
	protected function removeEnvFromBus($name){
		$fname = $this->dir.'/'.$name.'_env';
		unlink($fname);
	}
	
	protected function getFromQueue($name){
		$fname = $this->dir.'/'.$name.'_queue';
		$queue = $this->readFromFile($fname);
		if( $queue === false ) return false;
		$val = array_shift($queue);
		if( count($queue) > 0 )	$this->writeToFile($fname, $queue);
		else unlink($fname);
		return $val;
	}
	
	//gets from queue without removing
	protected function peekFromQueue($name){
		$fname = $this->dir.'/'.$name.'_queue';
		$queue = $this->readFromFile($fname);
		if( $queue === false ) return false;
		$val = array_shift($queue);
		return $val;
	}
	
	protected function putToQueue($name, $val){
		$fname = $this->dir.'/'.$name.'_queue';
		$queue = $this->readFromFile($fname);
		if( $queue === false ) $queue = array();
		array_push($queue, $val);
		$this->writeToFile($fname, $queue);
	}
	
	protected function removeQueue($name){
		$filename = $this->dir.'/'.$name.'_queue';
		if( file_exists($filename) ){
			unlink($filename);
		}
	}
	
}

?>
