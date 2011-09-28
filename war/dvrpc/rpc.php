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

require_once "config.php";
require_once "dvrpc.php";
require_once "dvservice.php";

//$db = new objdb($config['MYSQLHOST'], $config['MYSQLUSER'], $config['MYSQLPASS'], $config['MYSQLDBNAME']);

	/*functions:
	 * auth (auth, reg, etc.)
	 * getObject
	 * putObject
	*/


function randhash(){
	return md5(time()+mt_rand(0,10000));
}

function get_client_ip_address(){
	if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
		return $_SERVER['HTTP_X_FORWARDED_FOR'];
	} else if(isset($_SERVER['REMOTE_ADDR'])) {
		return $_SERVER['REMOTE_ADDR'];
	} else {
		return '0.0.0.0';
	}
}

function retarr($str){
	return array( 'result' => $str );
}

/*
 * sessionInit - Session initialization
 * returns:
 *   - struct 'objects': struct 'session': sid
 *   - result: 'success' on success, 'notreg' - company not found, error description on error
 */
// function sessionInit($method_name, $params, $app_data){

// 	$db = $app_data[0];
// 	$config = $app_data[1];
	
// 	$sid = randhash();
// 	$ip = get_client_ip_address();
	
// 	$ret = $db->putSession($sid, $ip, $config['SESSION_EXPIRATION_TIME_ANON']);
// 	if( $ret === false ){
// 		return retarr($db->getLastError());	
// 	} else {
// 		return array( 'objects' => array( 'session' => array('sid' => $sid) ), 'result' => 'success');
// 	}
	
// }

/*
 * util function for checking session
 */
// function checkSession($s, $db, $config){
// 	$sess = $db->getSession($s['sid']);
// 	if( $sess === false ){
// 		return false;
// 	}
	
// 	if( $sess['exp'] > 0 ){
// 		prolongSession($sess, $db, $config);
// 		return $sess;
// 	} else {
// 		$db->markSessionInactive($s['sid']);
// 		return false;		
// 	}
	
// }

// function prolongSession($sess, $db, $config){
// 	//uid of guest user == 1
// 	if( $sess['uid'] != 1 ){
// 		if( $sess['store']=='1' ) $db->prolongSession( $sess['sid'], $config['SESSION_EXPIRATION_TIME_AUTH_LONG'] );
// 		else $db->prolongSession( $sess['sid'], $config['SESSION_EXPIRATION_TIME_AUTH_SHORT'] );
// 	} else {
// 		$db->prolongSession( $sess['sid'], $config['SESSION_EXPIRATION_TIME_ANON'] );
// 	}
// }


/*
 * getObject - get Object
 * params:(struct)
 *   - (session)
 *   - (objects): dbid - id of object
 * returns:
 *   - struct 'objects': struct 'session': sid
 *   - result: 'success' on success, 'notreg' - company not found, error description on error
 */
/*function getObject($method_name, $params, $app_data){

	$db = $app_data[0];
	$conf = $app_data[1];
	$dbid = $params[0]['objects']['dbid'];
	
	$sess = checkSession($params[0]['session'], $db, $conf);
	if( $sess === false ) return array('result' => 'notsid');

	$obj = $db->getObject($dbid);
	if( $obj['uid'] === $sess['uid'] ){
		return array("result" => "success", "objects" => $obj);
	} else {
		
		if( in_array($obj['gid'], $sess['gids']) ){
			if( $obj['gr'] === '1' ) return array("result" => "success", "objects" => $obj);
		}
		
		if( $obj['or'] === '1' ) return array("result" => "success", "objects" => $obj);
		else return array( "result" => "permden" );
	}

}*/

#$xmlrpc_server = xmlrpc_server_create();

#xmlrpc_server_register_method($xmlrpc_server, "sessionInit", "sessionInit");
#xmlrpc_server_register_method($xmlrpc_server, "getObject", "getObject");

$requestData = $HTTP_RAW_POST_DATA;
//$GLOBALS['HTTP_RAW_POST_DATA'] 

$dvservice = new DVService();
$dvrpc = new DVRPC();
$dvrpc->registerService($dvservice);
$dvrpc->callService($requestData);

//system( "echo '$request_xml' > /home/www/saas/lastreq-".date("H-i-s-u")."-".md5($request_xml).".xml" );
// system( "echo '$requestData' > /home/anton/devel/workspace/dvijok/war/dvrpc/lastreq.txt" );

/*$headers = apache_request_headers();

system( "echo \"\" > /home/www/saas/lastreq.xml" );
foreach ($headers as $header => $value) {
	system( "echo \"$header: $value\n\" >> /home/www/saas/lastreq.xml" );
}*/

// $response = xmlrpc_server_call_method($xmlrpc_server, $request_xml, array($db, $config), array("encoding" => "utf-8" , 'escaping'=>'markup'));

// Now we print the response for the client to read.
// print $response;

/*
 *	This method frees the resources for the server specified
 *	It takes one argument, a handle of a server created with
 *	xmlrpc_server_create().
 */
// xmlrpc_server_destroy($xmlrpc_server);
?>
