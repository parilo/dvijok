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

require_once "dvipcfiles.php";

class DVService {

	public function __construct(){
	}
	
	public function call($obj){
		
		$func = $obj['func'];
		
		if( $func == 'initSession' ){
			return $this->initSession();
		} else if( $func == "listenForEvent" ){
			return $this->listenForEvent();
		} else if( $func == "testIPC" ){
			return $this->testIPC();
		}
		
		$ret['result'] = 'function not found';
		return $ret;
		
	}
	
	private function testIPC(){
		$ipc = new DVIPCFiles();
		$event = $ipc->invokeEvent("testIPC event :)");
	}
	
	private function listenForEvent(){
		
		$ipc = new DVIPCFiles();
		$event = $ipc->listenForEvent();
	
		$ret['event'] = $event;
		$ret['result'] = 'success';
		return $ret;
	}
	
	private function initSession(){
		
		$ret['ccc'] = "444444444";
		$ret['ddddddd'] = "8888888888";
		$a['eeee'] = "123123123"; 
		$a['fffff'] = "4321";
		$ret['obj'] = $a;
		return $ret; 
	}
	
}

?>
