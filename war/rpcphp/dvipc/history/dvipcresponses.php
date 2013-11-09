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
	abstract protected function peekFromQueue($name);
	abstract protected function putToQueue($name, $val);
	abstract protected function removeQueue($name);
	abstract protected function newDVIPC($id);
	
	private $ipcid;
	private $sess;
	private $timeout;
	private $registeredstr;
	private $registeredipcstr;
	private $envlastused;
	private $islistening;
	
	public function __construct($ipcid, $timeout){
		$this->ipcid = $ipcid;
		$this->timeout = $timeout;
		
		$this->registeredstr = $this->ipcid.'_registered';//name of env holding queues
		$this->registeredipcstr = $this->ipcid.'_registered_ipc';//name of env holding ipcs
		$this->envlastused = $this->ipcid.'_lastused';//name of env holding lastused timestamp
		$this->islistening = $this->ipcid.'_listening';
	}
	
	private function getSession(){
		return randHash();
	}
	
	//add id in env
	// this env must be array
	private function addIdIntoEnv($id, $env){
		$reg = $this->getEnvFromBus($env);
// 		error_log("addtoenv: ->".$env."<- ->$id<- ->".print_r($reg,true)."<-"); 
		if( $reg === false ) $reg = array();
		if( !in_array($id, $reg) ) array_push($reg, $id);
		$this->putEnvToBus($env, $reg);
// 		$reg = $this->getEnvFromBus($env);
// 		error_log("afteraddtoenv: ->".$env."<- ->".print_r($reg,true)."<-"); 
	}
	
	// this env must be array
	private function removeIdFromEnv($id, $env){
		$reg = $this->getEnvFromBus($env);
		if( is_array($reg) ){
			unset($reg[array_search($id, $reg)]);
			$this->putEnvToBus($this->registeredstr, $reg);
		}
	}
	
	//register queue with id in this ipc
	public function register($id){
// 		error_log("register: ".$id);
		$this->islistening = $this->ipcid.'_'.$id.'_listening';
		$this->addIdIntoEnv($id, $this->registeredstr);
		$this->sess = $id;
	}
	
	public function unregister($id){
		$this->removeQueue($this->ipcid.'_'.$id);
		$this->removeIdFromEnv($id, $this->registeredstr);
	}
	
	//register ipc with id in this ipc
	public function registerIPC($id){
		$this->addIdIntoEnv($id, $this->registeredipcstr);
	}
	
	public function unregisterIPC($id){
		$this->removeIdFromEnv($id, $this->registeredipcstr);
	}
	
	public function clear(){
// 		error_log(callTrace());
		//clearing queues
		$reg = $this->getEnvFromBus($this->registeredstr);
		if( $reg !== false ){
			foreach( $reg as $sess ){
				$this->removeQueue($this->ipcid.'_'.$sess);
			}
			$this->removeEnvFromBus($this->registeredstr);
		}

		//clearing ipc
		$reg = $this->getEnvFromBus($this->registeredipcstr);
		if( $reg !== false ){
			foreach( $reg as $id ){
				$ipc = $this->newDVIPC($id);
				$ipc->clear();
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
		return $this->getFromQueue($this->ipcid.'_'.$sess);
	}
	
	public function getEvent(){
		return $this->getEventFromBus($this->sess);
	}
	
	//gets without removing first event from queue
	private function peekEventFromQueue($id){
		return $this->peekFromQueue($this->ipcid.'_'.$id);
	}
	
	private function isQueueLive($id){
		$event = $this->peekEventFromQueue($id);
		if( $event === false ) return true;
		$ts = date_timestamp_get(date_create());
		//error_log("event: ".print_r($event, TRUE)."\n");
		if( ($ts - $event['ts']) > 60 ) return false;//1 min
		else return true;
	}
	
	private function isListening(){
		$res = $this->getEnvFromBus($this->islistening);
		if( $res === false ) return false;
		else return true;
	}
	
	private function markListening(){
		$this->putEnvToBus($this->islistening, "l");
	}
	
	private function unmarkListening(){
		$this->removeEnvFromBus($this->islistening);
	}
	
	private function haveUsingIPCs(){
		$flag = false;//need check all children ipcs
		$reg = $this->getEnvFromBus($this->registeredipcstr);
		if( is_array($reg) )
		foreach( $reg as $ipcid ){
			$ipc = $this->newDVIPC($ipcid);
			if( $ipc->isNotUsing() ){
				$ipc->clear();
				$this->unregisterIPC($ipcid);
			} else {
				$flag = true;
			}
		}
				
		return $flag;
	}
	
	private function saveLastUsed(){
		$this->putEnvToBus($this->envlastused, date_timestamp_get(date_create()));
	}
	
	public function getLastUsed(){
		return $this->getEnvFromBus($this->envlastused);
	}
	
	public function isNotUsing(){
		if( $this->haveUsingIPCs() ) return false;
		else {
			if( $this->isListening() ) return false;
			else {
				$now = date_timestamp_get(date_create());
				if( $now - $this->getLastUsed() > 300 ) return true;
				else return false;
			}
			
		}
		
	}
	
	public function listenForEvent(){
		
		$this->saveLastUsed();
		$this->markListening();
		
		// 1 min
		for($i=0; $i<600; $i++){//600
			$event = $this->getEventFromBus($this->sess);
			if( !($event === false) ){
				//error_log("unmark event:".$event['type']."\n");
				$this->unmarkListening();
				return $event;
			}
			usleep(100000);
		}
		
		//error_log("unmark idle\n");
		$this->unmarkListening();
		$event['type'] = 'idle';
		return $event;
	}
	
	public function invokeEvent($event){
		$event['ts'] = date_timestamp_get(date_create());
		
		//sending event to queues
		$reg = $this->getEnvFromBus($this->registeredstr);
		if( is_array($reg) )
		foreach( $reg as $sess ){
			if( $this->isQueueLive($sess) ){
				//put event if is live
				$this->putToQueue($this->ipcid.'_'.$sess, $event);
			} else {
				//unregister if this queue is not live
				$this->unregister($sess);
			}
		}
		
		//sending event to children ipcs
		$reg = $this->getEnvFromBus($this->registeredipcstr);
		if( is_array($reg) )
		foreach( $reg as $ipcid ){
			$ipc = $this->newDVIPC($ipcid);
			if( $ipc->isNotUsing() ){
// 				error_log("clearing: ".$ipcid);
				$ipc->clear();
				$this->unregisterIPC($ipcid);
			} else {
// 				error_log("invoking: ");
				$ipc->invokeEvent($event);
			}
		}
		
		
	}
	
}

?>
