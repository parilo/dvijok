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

require_once 'dvipc/dvipc.php';
require_once 'lib.php';

abstract class DVIPCResponses implements DVIPC {

	abstract protected function getEnvFromBus($name);
	abstract protected function putEnvToBus($name, $val);
	abstract protected function removeEnvFromBus($name);
	abstract protected function getFromQueue($name);
	abstract protected function putToQueue($name, $val);
	abstract protected function removeQueue($name);
	
	private $ipcid;
	private $sess;
	private $timeout;
	private $registeredstr;
	
	public function __construct($ipcid, $timeout){
		$this->ipcid = $ipcid;
		$this->timeout = $timeout;
		
		$this->registeredstr = $this->ipcid.'_registered';
	}
	
	private function getSession(){
		return randHash();
	}
	
	public function register($id){
		$reg = $this->getEnvFromBus($this->registeredstr); // this env must be array
		$sess = $id;
		if( $reg === false ) $reg = array();
		if( !in_array($sess, $reg) ) array_push($reg, $sess);
		$this->putEnvToBus($this->registeredstr, $reg);
		$this->sess = $sess;
	}
	
	public function unregister($id){
		$this->removeQueue($this->ipcid.'_'.$id);
		$reg = $this->getEnvFromBus($this->registeredstr); // this env must be array
		if( is_array($reg) ){
			unset($reg[array_search($id, $reg)]);
			$this->putEnvToBus($this->registeredstr, $reg);
		}
	}
	
	public function clear(){
		$reg = $this->getEnvFromBus($this->registeredstr);
		if( $reg !== false ){
			foreach( $reg as $sess ){
				$this->removeQueue($this->ipcid.'_'.$sess);
			}
			$this->removeEnvFromBus($this->registeredstr);
		}
	}
	
	private function registerOnBus(){
		$sess = $this->getSession();
		$this->register($sess);
		return $sess;
	}
	
	private function unregisterOnBus(){
		$this->unregister($this->sess);
	}
	
	private function getEventFromBus($sess){
		//remove old by timestamp $event['ts'];
		return $this->getFromQueue($this->ipcid.'_'.$sess);
	}
	
	public function getEvent(){
		return $this->getEventFromBus($this->sess);
	}
	
	public function listenForEvent(){
		// 1 min
		for($i=0; $i<600; $i++){
			$event = $this->getEventFromBus($this->sess);
			if( !($event === false) ){
				return $event;
			}
			usleep(100000);
		}
		
		$event['type'] = 'idle';
		return $event;
	}
	
	public function invokeEvent($event){
		$event['ts'] = date_timestamp_get(date_create());
		$reg = $this->getEnvFromBus($this->registeredstr);
		if( is_array($reg) )
		foreach( $reg as $sess ){
			$this->putToQueue($this->ipcid.'_'.$sess, $event);
		}
	}
	
}

?>
