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

class DVClient {
	
	private $url;
	private $sid;
	private $proto;
	private $flib;

	public function __construct($sid = false){
		
		global $config;
		$this->url = $config['rpcurl'];
		
		$this->proto = new DVRPCProto();
		$this->flib = new FetcherLib();
		
		if( $sid == false ) $this->initSession();
		else {
			$this->sid = $sid;
			$this->checkSession();
		}
	}
	
	private function ifSuccess($result){
		
		if( isset($result['result']) ){
			if( $result['result'] == 'success' ){
				return true;
			}
		}
		return false;
	}
	
	private function initSession(){
		$result = $this->initSessionReq();

		if( $this->isSuccess($result) ){
			$this->sid = $result['objs']['sid'];
			storeSession();
			checkSession();
		}
		
// 		initSession( new Handler<Boolean>(){
// 			@Override
// 			public void onHandle(Boolean param) {
// 				checkSession();
// 				storeSession();
// 			}});
		echo print_r($res, true)."\n";
		exit(0);
	}
	
	private function initSessionReq() {
		
		$req['func'] = 'initSession';
		$reqstr = $this->proto->hashMapCode($req);
		$resstr = $this->flib->curlPostContent($this->url, $reqstr, true);
 		return $this->proto->hashMapDecode($resstr);
// 		DBObject dbo = new DBObject();
// 		dbo.put("func", "initSession");
		
// 		dbRequest.request(dbo, new DVRequestHandler<DBObject>(){

// 			@Override
// 			public void success(DBObject result) {
// 				if( result.getString("result").equals("success") ){
// 					sid = result.getDBObject("objs").getString("sid");
// 					storeSession();
// 					if( handler != null ) handler.onHandle(true);
// 				}
// 			}

// 			@Override
// 			public void fail(DBObject result) {
// 				Lib.alert("DataBaseDVRPC: B: init session failed: "+result);
// 			}});
	}
	
// 	private function checkSession(){
// 		checkSession(new DBObject(),  new DVRequestHandler<DBObject>(){
	
// 			@Override
// 			public void success(DBObject result) {
// 				inited.customEventOccurred(new CustomEvent(result));
// 			}
	
// 			@Override
// 			public void fail(DBObject result) {
// 			}
// 		});
// 	}
	
// 	public function checkSession($sid) {
// 		DBObject req = new DBObject();
// 		req.put("sid", sid);
// 		req.put("func", "checkSession");
// 		req.put("obj", params);
	
// 		dbEventsRequest = dbRequest.request(req, new DVRequestHandler<DBObject>(){
	
// 			@Override
// 			public void success(DBObject result) {
// 				String res = result.getString("result");
// 				if( res.equals("success") ){
// 					handler.success(result.getDBObject("objs"));
// 				} else {
// 					//					if(
// 					checkNotSid(res, new Handler<Boolean>(){
// 						@Override
// 						public void onHandle(Boolean param) {
// 							checkSession(params, handler);
// 						}
// 					});
// 					//					) Lib.alert("DataBase: resetEvents A: fail: ->"+result+"<-");
// 				}
// 			}
	
// 			@Override
// 			public void fail(DBObject result) {
// 				Lib.alert("DataBase: resetEvents B: fail: "+result);
// 			}
// 		});
// 	}
	


}

?>
