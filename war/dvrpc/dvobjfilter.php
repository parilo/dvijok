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

require_once 'dvuser.php';

class DvObjFilter {

	/*
	 * $tags - array 
	 */
	public function filter($objs, $user, $tags){
		
		$t = array('estateAdsOwnerGive', 'estateAdsTake', 'estateAdsProbablyGive', 'estate');
		if( count(array_intersect($t, $tags)) > 0 ) {
		
			if( !DvUser::ismoderator($user) ){
	// 		if( $user['uid'] != 'moderator' ){
				$filtered = array();
				if( isset($objs['_isarr']) ){
					unset($objs['_isarr']);
					$filtered['_isarr'] = '1';
				}
				foreach( $objs as $ind => $obj ){
	 				if( is_array($obj) ){
	 					if( isset($obj['link']) ) unset($obj['link']);
	 					if( isset($obj['no']) ) unset($obj['no']);
	 					if( isset($obj['srcid']) ) unset($obj['srcid']);
	 				}
	 				$filtered[] = $obj;
				}
				return $filtered;
				
			}
		
		}
		
		return $objs;
	}

}

?>
