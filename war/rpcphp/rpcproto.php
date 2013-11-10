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

interface RPCProto {
	
	/**
	 * dbo - dictionary type object
	 * string -> php array
	 * @param unknown $data
	 */
	public function dboDecode($data);
	
	/**
	 * dbo - dictionary type object
	 * php array -> string
	 * @param unknown $dbo
	 */
	public function dboCode($dbo);
	
	/**
	 * dba - array type object
	 * string -> php array
	 * @param unknown $data
	 */
	public function dbaDecode($data);
	
	/**
	 * dba - array type object
	 * php array -> string
	 * @param unknown $array
	 */
	public function dbaCode($array);
	
	public function getName();
	
}

?>
