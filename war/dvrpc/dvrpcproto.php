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

class DVRPCProto {

	private $i;
	private $req;
	
	public function __construct(){
	}
	
	private function setData($request){
		$this->req = $request;
		$this->i = 0;
	}
	
	private function extractLen(){
		$pos = strpos($this->req, ",", $this->i);
		if( $pos === false ) return false;
		$len = substr($this->req, $this->i, $pos - $this->i );
		if( is_numeric($len) ){
			
			$this->i = $pos+1;
			
			return $len;
		}
		else return false;
	}
	
	private function extract($len){
		$ret = substr($this->req, $this->i, $len);
		if( $ret === false ) return false;
		$this->i += $len;
		return $ret;
	}
	
	private function hashMapGetIdent(){
		$len = $this->extractLen();
		if( $len === false ) return false;
		return $this->extract($len);
	}
	
	private function hashMapGetType(){
		return $this->extract(3);
	}
	
	private function hashMapGetVal(){
		return $this->hashMapGetIdent();	
	}
	
	public function hashMapDecode($data){
		$this->setData($data);
		$ret = array();
		while(
			($ident = $this->hashMapGetIdent()) !== false &&
			($type = $this->hashMapGetType()) !== false &&
			($val = $this->hashMapGetVal()) !== false
		){
			if( $type == "STR" ){
				$ret[$ident] = $val;
			} else if( $type == "DBO" ){
				$currI = $this->i;
				$currReq = $this->req;
				$ret[$ident] = $this->hashMapDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			} else if( $type == "DBA" ){
				$currI = $this->i;
				$currReq = $this->req;
				$ret[$ident] = $this->arrayDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			}
		}
		
		return $ret;
	}
	
	public function hashMapCode($hashMap){
		$ret = "";
		foreach( $hashMap as $key => $val ){
			if( is_string($val) ){
				$ret .= strlen($key).",".$key."STR".strlen($val).",".$val;
			} else if( is_array($val) && array_key_exists('_isarr',$val) ){
				$str = $this->arrayCode($val);
				$ret .= strlen($key).",".$key."DBA".strlen($str).",".$str;
			} else if( is_array($val) ){
				$str = $this->hashMapCode($val);
				$ret .= strlen($key).",".$key."DBO".strlen($str).",".$str;
			}
		}
		return $ret;
	}
	
	public function arrayDecode($data){
		$this->setData($data);
		$ret = array();
		while(
			($type = $this->hashMapGetType()) !== false &&
			($val = $this->hashMapGetVal()) !== false
		){
			if( $type == "STR" ){
				$ret[] = $val;
			} else if( $type == "DBO" ){
				$currI = $this->i;
				$currReq = $this->req;
				$ret[] = $this->hashMapDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			} else if( $type == "DBA" ){
				$currI = $this->i;
				$currReq = $this->req;
				$ret[] = $this->arrayDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			}
		}
		
		$ret['_isarr'] = '_isarr';
		return $ret;
	}
	
	public function arrayCode($array){
		$ret = "";
		foreach( $array as $val ){
			if( is_string($val) && $val != "_isarr" ){
				$ret .= "STR".strlen($val).",".$val;
			} else if( is_array($val) && array_key_exists('_isarr',$val) ){
				$str = $this->arrayCode($val);
				$ret .= "DBA".strlen($str).",".$str;
			} else if( is_array($val) ){
				$str = $this->hashMapCode($val);
				$ret .= "DBO".strlen($str).",".$str;
			}
		}
		return $ret;
	}
}

?>
