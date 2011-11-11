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

require_once "config.def.php";
require_once "dvrpc.php";
require_once "dvservice.php";

function get_client_ip_address(){
	if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
		return $_SERVER['HTTP_X_FORWARDED_FOR'];
	} else if(isset($_SERVER['REMOTE_ADDR'])) {
		return $_SERVER['REMOTE_ADDR'];
	} else {
		return '0.0.0.0';
	}
}

$requestData = $HTTP_RAW_POST_DATA;
//$GLOBALS['HTTP_RAW_POST_DATA'] 

//system( "echo '$request_xml' > /home/www/saas/lastreq-".date("H-i-s-u")."-".md5($request_xml).".xml" );
global $config;
if( isset($config['lastreq']) ){
	$lastreq = $config['lastreq'];
	system( "echo '$requestData' > $lastreq" );
}

try {
	
	$proto = new DVRPCProto(); 
	$dvservice = new DVService();
	$dvrpc = new DVRPC();
	$dvrpc->setProto($proto);
	$dvrpc->registerService($dvservice);
	$dvrpc->callService($requestData, get_client_ip_address());

} catch ( DBException $ex ){
	//echo "aaaaaaaaaaaaaaaaaaa\n";
	//echo $ex->getTraceAsString();
	$ret['result'] = 'notdb';
	echo $proto->hashMapCode($ret)."\n";
}

/*$headers = apache_request_headers();

system( "echo \"\" > /home/www/saas/lastreq.xml" );
foreach ($headers as $header => $value) {
	system( "echo \"$header: $value\n\" >> /home/www/saas/lastreq.xml" );
}*/

?>
