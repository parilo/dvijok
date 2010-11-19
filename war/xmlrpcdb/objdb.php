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
	
}

 ?>