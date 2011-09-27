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

require_once "dvipc.php";
require_once "config.php";

class DVIPCShm implements DVIPC {

	private $key;
	
	public function __construct(){
		$key = 100;//$config['shmemkey'];
	}
	
	public function listenForEvent(){
		$shmres = shm_attach($this->key);
		while( shm_has_var($shmres, 1) === false ){
			print "a: ".shm_has_var($shmres, 1)."\n";
			usleep(100000);
		}
		print "event\n";
// 		do{
// 			$var = shm_get_var($shmres, 1);
// 		} while( $var != "aaaeventvar" );
		return "event";
	}
}

?>
