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
		
		$user2['uid'] = 'test2';
		$usergids2[] = 'test2';
		$usergids2[] = 'group2';
		$user2['gids'] = $usergids2;
		
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
		
		$dbo2['field1'] = 'aaa1';
		$dbo2['field2'] = 'aaa2';
				
		$id1 = $db->putObject($dbo1, 'aaa bbb ccc', $user, $rights);
		$id2 = $db->putObject($dbo2, 'bbb ccc', $user, $rights);
		$id3 = $db->putObject($dbo2, 'bbb ccc', $user, $rights);
		
  		$db->delObject($id3);

  		$id3 = $db->putObject($dbo2, 'ccc', $user, $rights);
  		
  		print 'by id: ->'.serialize($db->getObjectById($id2, $user))."<-\n";
  		print 'by id: ->'.serialize($db->getObjectById($id2, $user2))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectByTags('ccc', $user))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectByTags('bbb', $user))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectByTags('aaa', $user))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectByTags('bbb ccc', $user))."<-\n";

  		print "\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('bbb', $user))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('aaa', $user))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('bbb ccc', $user))."<-\n";
  		
  		print "\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user, 1))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user, 2))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user, 1, 1))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user, 1, 10))."<-\n";
  		
  		print "\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user2))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('bbb', $user2))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('aaa', $user2))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('bbb ccc', $user2))."<-\n";
  		  		
  		print "\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user2, 1))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user2, 2))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user2, 1, 1))."<-\n";
  		print 'by tags: ->'.serialize($db->getObjectsByTags('ccc', $user2, 1, 10))."<-\n";
  		  		
  		$db->delObject($id1);
  		$db->delObject($id2);
  		$db->delObject($id3);
  		
	}
}

new DataBaseTest();

?>
