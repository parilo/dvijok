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
		if( file_exists($filename) ){
// 			$file = fopen($filename, 'r');
// 			$content = fread($file, filesize($filename));
			$content = file_get_contents($filename);
			print "cont: ->$content<-\n";
// 			fclose($file);
			return unserialize($content);
		} else return false;			
	}
	
	private function writeToFile($filename, $val){
		$file = fopen($filename, 'w');
		$serval = serialize($val);
		print "write: ->$serval<-\n";
// 		fwrite($file, $serval);
// 		fflush($file);
// 		fclose($file);
		file_put_contents($filename, $serval);
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
	
	protected function putToQueue($name, $val){
		$fname = $this->dir.'/'.$name.'_queue';
		$queue = $this->readFromFile($fname);
		print "q: $queue\n";
		if( $queue === false ) $queue = array();
		array_push($queue, $val);
// 		$queue = unserialize(serialize($queue));
// 		array_push($queue, "aaa");
// 		$queue = unserialize(serialize($queue));
// 		array_push($queue, "bbb");
		$this->writeToFile($fname, $queue);
	}
	
}

?>
