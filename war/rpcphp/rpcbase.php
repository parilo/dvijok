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

// require_once 'config.php';

class RPC {
	
	private function getClientIPAddress(){
		if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
			return $_SERVER['HTTP_X_FORWARDED_FOR'];
		} else if(isset($_SERVER['REMOTE_ADDR'])) {
			return $_SERVER['REMOTE_ADDR'];
		} else {
			return '0.0.0.0';
		}
	}
	
	public static function getProto(){
		global $config;
		if( isset($config['rpcType']) ){
			if( $config['rpcType'] == 'json' ){
				require_once "jsonproto.php";
				return new JSONProto();
			}
			else return new DVRPCProto();
		} else return new DVRPCProto();
		
	}
	
	public function process(){

		$requestData = file_get_contents('php://input');
		
		global $config;
		if( isset($config['lastreq']) ){
			$lastreq = $config['lastreq'];
			//save last req
			//system( "echo '$requestData' > $lastreq" );
			//system( "echo '$request_xml' > /home/www/saas/lastreq-".date("H-i-s-u")."-".md5($request_xml).".xml" );
		}
		
		try {
			 
			$dvservice = new DVService();
			$dvrpc = new DVRPC();
			$dvrpc->setProto(RPC::getProto());
			$dvrpc->registerService($dvservice);
			$dvrpc->callService($requestData, $this->getClientIPAddress());
		
		} catch ( DBException $ex ){
			$ret['result'] = '->'.$ex->getValue().'<- '.$ex->getTraceAsString();
			echo RPC::getProto()->dboCode($ret)."\n";
		}

	}



}

?>
