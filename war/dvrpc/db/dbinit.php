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

class DataBaseInit {

	public function __construct(){
	}

	public function init($db){
		
		$root['uid'] = 'root';
		$rootgids[] = 'root';
		$root['gids'] = $rootgids;
		$root['nologin'] = '1';
		
		$guest['uid'] = 'guest';
		$guestgids[] = 'guest';
		$guest['gids'] = $guestgids;
		$guest['nologin'] = '1';
		
// 		$moderator['uid'] = 'moderator';
// 		$moderatorgids[] = 'moderator';
// 		$moderator['gids'] = $moderatorgids;
// 		$moderator['pass'] = 'igpd284kjf';
				
// 		$rights['uid'] = 'root';
// 		$rights['gid'] = 'root';
// 		$rights['ur'] = '1';
// 		$rights['uw'] = '1';
// 		$rights['gr'] = '0';
// 		$rights['gw'] = '0';
// 		$rights['or'] = '0';
// 		$rights['ow'] = '0';
		
		//tag rights
// 		$tr['uid'] = 'root';
// 		$tr['gid'] = 'root';
// 		$tr['uw'] = '1';
// 		$tr['gw'] = '1';
// 		$tr['ow'] = '1';
		
		//userinfos
// 		$moderatorui['username'] = 'moderator';
// 		$moderatorui['ismoderator'] = '1';

// 		$muirights['uid'] = 'root';
// 		$muirights['gid'] = 'moderator';
// 		$muirights['ur'] = '1';
// 		$muirights['uw'] = '1';
// 		$muirights['gr'] = '1';
// 		$muirights['gw'] = '1';
// 		$muirights['or'] = '0';
// 		$muirights['ow'] = '0';
		
		$db->putObject($root, 'user admin'/*, $root, $rights*/);
// 		$db->putObject_($moderator, 'user moderator', $root, $rights);
// 		$db->putObject_($moderatorui, 'userinfo moderator', $root, $muirights);
		$db->putObject($guest, 'user guest'/*, $root, $rights*/);
		
		//$db->putTags('comment maincomments', $tr);
		
		$db->setInitialized();
		
	}
}

?>
