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

class JSONProto implements RPCProto {

	private function removeIsArr($obj){
		foreach ($obj as $key => $val) {
			if( is_array($val) ) $obj[$key] = $this->removeIsArr($val);
			else if( $key == '_isarr' ) unset($obj[$key]);
		}
		return $obj;
	}
	
	public function dboDecode($data){
		return json_decode($data, true);
	}
	
	public function dboCode($dbo){
		return json_encode($this->removeIsArr($dbo));
	}
	
	public function dbaDecode($data){
		return json_decode($data, true);
	}
	
	public function dbaCode($array){
		return json_encode($this->removeIsArr($array));
	}

	public function getName(){
		return "json";
	}
	
}

?>
