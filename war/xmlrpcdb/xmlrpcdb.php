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



	/*functions:
	 * auth (auth, reg, etc.)
	 * getObject
	 * putObject
	*/

function hello($method_name, $params, $app_data){
	return "Hi! I'm dvijok db xmlrpc ^_^";
}


$xmlrpc_server = xmlrpc_server_create();

xmlrpc_server_register_method($xmlrpc_server, "hello", "hello");

$request_xml = $HTTP_RAW_POST_DATA;
//$GLOBALS['HTTP_RAW_POST_DATA'] 

//system( "echo '$request_xml' > /home/www/saas/lastreq-".date("H-i-s-u")."-".md5($request_xml).".xml" );
//system( "echo '$request_xml' > /home/www/saas/lastreq.xml" );

/*$headers = apache_request_headers();

system( "echo \"\" > /home/www/saas/lastreq.xml" );
foreach ($headers as $header => $value) {
	system( "echo \"$header: $value\n\" >> /home/www/saas/lastreq.xml" );
}*/

$response = xmlrpc_server_call_method($xmlrpc_server, $request_xml, array($db, $config), array("encoding" => "utf-8" , 'escaping'=>'markup'));

// Now we print the response for the client to read.
print $response;

/*
 *	This method frees the resources for the server specified
 *	It takes one argument, a handle of a server created with
 *	xmlrpc_server_create().
 */
xmlrpc_server_destroy($xmlrpc_server);
?>
