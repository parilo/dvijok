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

interface DVIPC {

	public function register($id);//register queue
	public function unregister($id);//removes $id's queue
	
	public function registerIPC($id);//register another IPC with id
	public function unregisterIPC($id);//removes another IPC with id
	
	public function getEvent();
	public function listenForEvent();
	public function invokeEvent($event);
	public function clear();//removes all ipc meta(files, etc.)
	public function isNotUsing();//returns true if this IPC is currently not using by user and can be removed from IPC system
	public function getLastUsed();//returns timestamp of last call listenForEventFunction()
}

?>
