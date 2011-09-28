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

require_once "dvipcresponses.php";
require_once "lib.php";

class DVIPCFiles extends DVIPCResponses {

	private $dir;
	
	function __construct(){
		$this->dir = '/home/anton/devel/workspace/dvijok/war/dvrpc/ipcfiles/';
	}
	
	private function readFromFile($filename){
		$file = fopen($filename, 'r');
		$content = fread($file, filesize($filename));
		fclose($file);
		return unserialize($content);
	}
	
	private function writeToFile($filename, $val){
		$file = fopen($filename, 'w');
		fwrite($file, serialize($val));
		fclose($file);
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
		$this->writeToFile($fname, $queue);
		return $val;
	}
	
	protected function putToQueue($name, $val){
		$fname = $this->dir.'/'.$name.'_queue';
		$queue = $this->readFromFile($fname);
		if( $queue === false ) $queue = array();
		array_push($queue, $val);
		$this->writeToFile($fname, $queue);
	}
	
}

?>
