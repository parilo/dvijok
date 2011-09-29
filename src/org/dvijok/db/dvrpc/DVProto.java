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
//

package org.dvijok.db.dvrpc;

import org.dvijok.lib.Lib;

public class DVProto {

	private int i;
	private String req;
	
	public void setRequestString(String request){
		req = request;
		i = 0;
	}
	
	private int extractLen(){
		int pos = req.indexOf(",", i);
		if( pos == -1 ) return -1;
		String lenS = req.substring(i, pos-i);
		try{
			int len = Integer.parseInt(lenS);
			i = pos+1;
			return len;
		} catch (NumberFormatException e){
			return -1;
		}
	}
	
	private function extract($len){
		$ret = substr($this->req, $this->i, $len);
		$this->i += $len;
		return $ret;
	}
	
	private function requestGetIdent(){
		$len = $this->extractLen();
		if( $len === false ) return false;
		$ident = $this->extract($len);
		if( $ident === false ) return false;
		else return $ident;
	}
	
	private function requestGetType(){
		return $this->extract(3);
	}
	
	private function requestGetVal(){
		return $this->requestGetIdent();	
	}
	
	public function requestDecode(){
		$ret = array();
		while(
			($ident = $this->requestGetIdent()) !== false &&
			($type = $this->requestGetType()) !== false &&
			($val = $this->requestGetVal()) !== false
		){
			if( $type == "STR" ){
				$ret[$ident] = $val;
			} else if( $type == "DBO" ){
				$proto = new DVRPCProto();
				$proto->setRequestString($val);
				$ret[$ident] = $proto->hashMapDecode();
			} else if( $type == "DBA" ){
				$proto = new DVRPCProto();
				$proto->setRequestString($val);
				$ret[$ident] = $proto->arrayDecode();
			}
		}
		
		return $ret;
	}
	
	//HashMap
	
	private function hashMapGetIdent(){
		return $this->requestGetIdent();
	}
	
	private function hashMapGetType(){
		return $this->requestGetType();
	}
	
	private function hashMapGetVal(){
		return $this->requestGetVal();
	}

	public function hashMapDecode(){
		return $this->requestDecode();
	}
	
	public function hashMapCode($hashMap){
		$ret = "";
		foreach( $hashMap as $key => $val ){
			if( is_string($val) ){
				$ret .= strlen($key).",".$key."STR".strlen($val).",".$val;
			} else if( is_array($val) && array_key_exists('_isarr',$hashMap) ){
				$str = $this->arrayCode($val);
				$ret .= strlen($key).",".$key."DBA".strlen($str).",".$str;
			} else if( is_array($val) ){
				$str = $this->hashMapCode($val);
				$ret .= strlen($key).",".$key."DBO".strlen($str).",".$str;
			}
		}
		return $ret;
	}
	
	public function arrayDecode(){
		$arr['array'] = 'need to parse :) ';
	}
	
	public function arrayCode($array){
		return "need to code array :)";
	}
}
