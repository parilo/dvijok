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

require_once 'config.php';

function get_client_ip_address(){
	if (isset($_SERVER['HTTP_X_FORWARDED_FOR'])) {
		return $_SERVER['HTTP_X_FORWARDED_FOR'];
	} else if(isset($_SERVER['REMOTE_ADDR'])) {
		return $_SERVER['REMOTE_ADDR'];
	} else {
		return '0.0.0.0';
	}
}

if( isset($_FILES['fileupload']) ){

	global $config;

	$uploaddir = $config['uploaddir'];
	$uploadfile = $uploaddir .'/'. basename($_FILES['fileupload']['name']);
	
	if (move_uploaded_file($_FILES['fileupload']['tmp_name'], $uploadfile)) {
		
		require_once "dbget.php";
		$db = getDB();
		$mdb = $db->getDB();
		
		$sid = $mdb->real_escape_string($_POST['sid']);
		$fileid = $mdb->real_escape_string($_POST['fileid']);
		$filename = $mdb->real_escape_string($uploadfile);
		
		$sql = "INSERT INTO dvuploadedfiles(filename, sid, fileid) VALUES ('$filename', '$sid', '$fileid')";
		$result = $mdb->query($sql);
		if (!$result) throw new DBException('mysql error (' . $mdb->errno . ') '. $mdb->error. '  '.$sql);
				
		echo "uploaded\n";
	} else {
		echo "failed\n";
// 		echo "Возможная атака с помощью файловой загрузки!\n";
	}

}

?>
