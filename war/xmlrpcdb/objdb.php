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


require_once "db.php";

class objdb extends database {
	
	public function putSession($sid, $ip, $sess_exp_time ){
			
		$sess['sid'] = $sid;
		$sess['uid'] = '1';
		$sess['ip'] = $ip;
		$sess['active'] = '1';
		$sess['begin_date'] = 'NOW()';
		$sess['end_date'] = "DATE_ADD(NOW(), INTERVAL $sess_exp_time MINUTE)";
		
		$sesst['sid'] = 'str';
		$sesst['uid'] = 'int';
		$sesst['ip'] = 'str';
		$sesst['active'] = 'int';
		$sesst['begin_date'] = 'func';
		$sesst['end_date'] = 'func';
		
		$ret = $this->insert('session',$sess, $sesst);
		return $ret;
		
	}
	
	public function getSession($sid){
		return $this->select(
			'session',
			array('id, sid, uid, comp_id, id_webdata, ip, store, active, begin_date, unix_timestamp(end_date)-unix_timestamp(NOW()) as exp' ),
			null,
			array( 'sid' => $sid, 'active' => '1' ),
			array( 'sid' => 'str', 'active' => 'int' )
		);
	}
	
	public function prolongSession($sid, $exp_time){
		return $this->update(
			'session',
			null,
			array( 'end_date' => "DATE_ADD(NOW(), INTERVAL $exp_time MINUTE)" ),
			array( 'end_date' => 'func' ),
			array( 'sid' => $sid ),
			array( 'sid' => 'str' )
		);
	}
	
	public function getObject($dbid){
		$obj = $this->select(
			'objects o',
			array( 'uid, gid' ),
			null,
			array( 'o.dbid' => $dbid ),
			array( 'o.dbid' => 'str' )
		);

		if( $obj === false ) return false;
		
		$ret = $this->select_bulk(
			'objects o',
			array( 'field, value' ),
			array( 'object_fields f' => array( 'l' => 'o.id', 'r' => 'f.id_object' ) ),
			array( 'o.dbid' => $dbid ),
			array( 'o.dbid' => 'str' )
		);
		
		if( $ret === false ) return false;
		
		for($i=0; $i<count($ret); $i++ )
			$obj[$ret[$i]['field']] = $ret[$i]['value'];
		
		$obj['dbid'] = $dbid;
		if( !isset($obj['ur']) ) $obj['ur'] = '1'; //user read
		if( !isset($obj['uw']) ) $obj['uw'] = '1'; //user write
		if( !isset($obj['gr']) ) $obj['gr'] = '1'; //group read
		if( !isset($obj['gw']) ) $obj['gw'] = '0'; //group write
		if( !isset($obj['or']) ) $obj['or'] = '1'; //other read
		if( !isset($obj['ow']) ) $obj['ow'] = '0'; //other write
		
		return $obj;
	}
	
}

?>