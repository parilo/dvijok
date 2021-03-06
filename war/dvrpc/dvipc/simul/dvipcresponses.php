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

//not used now
abstract class DVIPCResponses implements DVIPC {

	abstract protected function getEnvFromBus($name);
	abstract protected function putEnvToBus($name, $val);
	abstract protected function removeEnvFromBus($name);
	abstract protected function getFromQueue($name);
	abstract protected function putToQueue($name, $val);
	
	private $sess;
	private $timeout;
	
	public function __construct($timeout){
		$this->timeout = $timeout;
	}
	
	private function getSession(){
		return randHash();
	}
	
	private function registerOnBus(){
		$reg = $this->getEnvFromBus('registered'); // this env must be array
		$sess = $this->getSession();
		if( $reg === false ) $reg = array();
		array_push($reg, $sess);
		$this->putEnvToBus('registered', $reg);
		$this->sess = $sess;
		return $sess;
	}
	
	private function unregisterOnBus(){
		$reg = $this->getEnvFromBus('registered'); // this env must be array
		if( is_array($reg) ){
			unset($reg[array_search($this->sess, $reg)]);
			$this->putEnvToBus('registered', $reg);
		}
	}
	
	private function getEventFromBus($sess){
		return $this->getFromQueue($sess);
	}
	
	public function listenForEvent(){
		$sess = $this->registerOnBus();
		for(;;){
			$event = $this->getEventFromBus($sess);
			if( !($event === false) ){
				break;
			}
			usleep(100000);
		}
		$this->unregisterOnBus();
		return $event;
	}
	
	public function invokeEvent($event){
		$reg = $this->getEnvFromBus('registered');
		foreach( $reg as $sess ){
			$this->putToQueue($sess, $event);
		}
	}
	
}

?>
