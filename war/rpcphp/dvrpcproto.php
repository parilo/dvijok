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

require_once 'rpcproto.php';

class DVRPCProto implements RPCProto {

	private $i;
	private $req;
	
	public function __construct(){
	}
	
	private function setData($request){
		$this->req = $request;
		$this->i = 0;
	}
	
	private function strpos($haystack, $needle, $offset = 0){
#		echo "n: ->$needle<-, o: $offset len: ".strlen($haystack)." mblen: ".mb_strlen($haystack, mb_detect_encoding($haystack));
#		echo " enc: ".mb_detect_encoding($haystack)."\n";
#		echo "hs: $haystack, n: $needle, o: $offset\n";
#		return mb_strpos($haystack, $needle, $offset, mb_detect_encoding($haystack));
		return mb_strpos($haystack, $needle, $offset, 'UTF-8');
	}
	
	private function substr($str, $start ,$length){
#		return mb_substr($str, $start ,$length, mb_detect_encoding($str));
		return mb_substr($str, $start ,$length, 'UTF-8');
	}
	
	private function strlen($str){
// 		$enc = mb_detect_encoding($str);
// 		return mb_strlen($str, $enc?$enc:'UTF-8');
#		return mb_strlen($str, mb_detect_encoding($str));
		return mb_strlen($str, 'UTF-8');
	}
	
	private function extractLen(){
		if( $this->i >= $this->strlen($this->req) ) return false;
		$pos = $this->strpos($this->req, ",", $this->i);
		if( $pos === false ) return false;
		$len = $this->substr($this->req, $this->i, $pos - $this->i );
		if( is_numeric($len) ){
			
			$this->i = $pos+1;
			
			return $len;
		}
		else return false;
	}
	
	private function extract($len){
		$ret = $this->substr($this->req, $this->i, $len);
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
	
	public function dboDecode($data){
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
				$ret[$ident] = $this->dboDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			} else if( $type == "DBA" ){
				$currI = $this->i;
				$currReq = $this->req;
				$ret[$ident] = $this->dbaDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			}
		}
		
		return $ret;
	}
	
	public function dboCode($hashMap){
		$ret = "";
		foreach( $hashMap as $key => $val ){
// 			if( is_string($val) ){
// 				$ret .= mb_strlen($key).",".$key."STR".mb_strlen($val).",".$val;
// 			} else
			if( is_array($val) && array_key_exists('_nocode',$val) ){
				$ret .= $this->strlen($key).",".$key.$val['_nocodedata'];
			} else if( is_array($val) && array_key_exists('_isarr',$val) ){
				$str = $this->dbaCode($val);
				$ret .= $this->strlen($key).",".$key."DBA".$this->strlen($str).",".$str;
			} else if( is_array($val) ){
				$str = $this->dboCode($val);
				$ret .= $this->strlen($key).",".$key."DBO".$this->strlen($str).",".$str;
			} else {
				$ret .= $this->strlen($key).",".$key."STR".$this->strlen($val).",$val";
			}
		}
		return $ret;
	}

	public function dboCodeWrapped($hashMap){
		$str = $this->dboCode($hashMap);
		return 'DBO'.$this->strlen($str).",".$str;
	}
	
	public function dbaDecode($data){
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
				$ret[] = $this->dboDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			} else if( $type == "DBA" ){
				$currI = $this->i;
				$currReq = $this->req;
				$ret[] = $this->dbaDecode($val);
				$this->i = $currI;
				$this->req = $currReq;
			}
		}
		
		$ret['_isarr'] = '_isarr';
		return $ret;
	}
	
	public function dbaCode($array){
		$ret = "";
		foreach( $array as $key => $val ){
// 			if( is_string($val) && $key != "_isarr" ){
// 				$ret .= "STR".mb_strlen($val).",".$val;
// 			} else
			if( is_array($val) && array_key_exists('_isarr',$val) ){
				$str = $this->dbaCode($val);
				$ret .= "DBA".$this->strlen($str).",".$str;
			} else if( is_array($val) ){
				$str = $this->dboCode($val);
				$ret .= "DBO".$this->strlen($str).",".$str;
			} else if( "$key" != '_isarr' ) {
				$ret .= "STR".$this->strlen($val).",$val";
			}
		}
		
		return $ret;
	}
	
	public function getName(){
		return "dvrpc";
	}
	
}

?>
