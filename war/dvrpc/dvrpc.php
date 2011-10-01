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

require_once "dvrpcproto.php";

class DVRPC {

	private $proto; 
	private $dvservice;
	
	public function __construct(){
		$this->proto = new DVRPCProto();
	}
	
	public function registerService($dvservice){
		$this->dvservice = $dvservice;
	}
	
	private function requestParse($request){
		$req = $this->proto->hashMapDecode($request);
		
		$retobj = $this->dvservice->call($req);
		
		$ret = $this->proto->hashMapCode($retobj);
		print "$ret\n";
	}
	
	public function callService($request){
		$this->requestParse($request);
	}
	
}

?>
