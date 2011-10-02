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

require_once 'db.php';

class DataBaseTest {

	public function __construct(){
		$db = new DataBase();
		
		$user['uid'] = 'test';
		$usergids[] = 'test';
		$usergids[] = 'group1';
		$usergids[] = 'group2';
		$usergids[] = 'group3';
		$user['gids'] = $usergids;
		
		$rights['uid'] = 'test';
		$rights['gid'] = 'test';
		$rights['ur'] = '1';
		$rights['uw'] = '1';
		$rights['gr'] = '0';
		$rights['gw'] = '0';
		$rights['or'] = '0';
		$rights['ow'] = '0';
		
		$dbo1['field1'] = 'val1';
		$dbo1['field2'] = 'val2';
		
		$db->putObject($dbo1, '', $user, $rights);
	}
}

new DataBaseTest();

?>
