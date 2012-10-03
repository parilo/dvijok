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

require_once 'fetcherlib.php';
require_once 'dvrpcproto.php';

interface DVClientDB {
	public function getSid();
	public function saveSid($sid);
}

class DVClient {
	
	private $url;
	private $sid;
	private $proto;
	private $flib;
	private $dvclientdb;
	private $userinfo;
	private $userdata;
	
	public function __construct($url, $dvclientdb){
		
		$this->url = $url;
		
		$this->proto = new DVRPCProto();
		$this->flib = new FetcherLib();
		$this->dvclientdb = $dvclientdb;
		$this->sid = $dvclientdb->getSid();
		
		if( $this->sid === false ) $this->initSession();
		else $this->checkSession();
	}
	
	private function isSuccess($result){
		
		if( isset($result['result']) ){
			if( $result['result'] == 'success' ){
				return true;
			}
		}
		return false;
	}
	
	private function storeSession(){
		$this->dvclientdb->saveSid($this->sid);
	}
	
	private function initSession(){
		$result = $this->initSessionReq();

		if( $this->isSuccess($result) ){
			$this->sid = $result['objs']['sid'];
			$this->storeSession();
		}
	}
	
	private function initSessionReq() {
		$req['func'] = 'initSession';
		$reqstr = $this->proto->hashMapCode($req);
		$resstr = $this->flib->curlPostContent($this->url, $reqstr, true);
 		return $this->proto->hashMapDecode($resstr);
	}
	
	public function checkSession() {
		
		$req['func'] = 'checkSession';
		$req['sid'] = $this->sid;
		$reqstr = $this->proto->hashMapCode($req);
		$resstr = $this->flib->curlPostContent($this->url, $reqstr, true);
		$result = $this->proto->hashMapDecode($resstr);
		if( !$this->isSuccess($result) ) return $this->initSession();

		$this->userinfo = $result['objs']['userinfo'];
		$this->userdata = $result['objs']['userdata'];
		
	}
	
	public function isAuthorized(){
		return isset($this->userinfo['type']);
	}
	
	public function login($logpass){
		
		$loginp['login'] = $logpass['login'];
		$challange = $this->loginReq($loginp);
		$logpass['chal'] = $challange;
		$res = $this->loginResponseReq($logpass);
		
	}
	
	/*
	 * need $params['login']
	 */
	private function loginReq($params) {
		
		$req['func'] = 'login';
		$req['sid'] = $this->sid;
		$req['obj'] = $params;
		$reqstr = $this->proto->hashMapCode($req);
		$resstr = $this->flib->curlPostContent($this->url, $reqstr, true);
		$result = $this->proto->hashMapDecode($resstr);
		//echo $this->url."->$resstr<-\n";
		if( $result['result'] != 'challange' ){
			exit("dvclient login error. We have no challange: ".print_r($result, true));
		}
		return $result['chal'];
	}
	
	/*
	 * need $data['login'], $data['pass'] and $data['chal']
	 */
	private function loginResponseReq($data){
		
		$resp = md5($data['chal'].$data['pass']);
		
		$params['login'] = $data['login'];
		$params['response'] = $resp;

		$req['func'] = 'login';
		$req['sid'] = $this->sid;
		$req['obj'] = $params;
		$reqstr = $this->proto->hashMapCode($req);
		$resstr = $this->flib->curlPostContent($this->url, $reqstr, true);
		$result = $this->proto->hashMapDecode($resstr);
		
		if( $result['result'] != 'success' ){
			exit("cannot login: ".print_r($result, true));
		}
		
	}

	public function external($params){

		$req['func'] = 'external';
		$req['sid'] = $this->sid;
		$req['obj'] = $params;
		$reqstr = $this->proto->hashMapCode($req);
		$resstr = $this->flib->curlPostContent($this->url, $reqstr, true);
# echo "$resstr\n";
		$result = $this->proto->hashMapDecode($resstr);
		
		return $result;
	}

}

?>
