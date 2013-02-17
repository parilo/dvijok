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

function randHash(){
	return md5(time()+mt_rand(0,10000));
}

function isValidMd5($md5){
	return !empty($md5) && preg_match('/^[a-f0-9]{32}$/', strtolower($md5));
}

function fileWrite($filename, $data){
	clearstatcache();
	$res = fopen($filename, 'w');
	if( $res === false ) return false;

	if (flock($res, LOCK_EX)) {
		fwrite($res, $data);
		fflush($res);
		flock($res, LOCK_UN);
		fclose($res);
		return true;
	} else return false;
}

function fileRead($filename){
	clearstatcache();
	$handle = fopen($filename, "r");
	if( $handle === false ) return false;

	if (flock($handle, LOCK_EX)) {
		$fsize = filesize($filename);
		if( $fsize > 0 ) $contents = fread($handle, filesize($filename));
		else $contents = '';
		fclose($handle);
		return $contents;
	} else return false;
}

function callTrace(){
	$e = new Exception;
	return print_r($e->getTraceAsString(),true);
}

function stripSpace($str){
	return preg_replace ( '/\s+/', ' ', $str );
}

function objcmpAsc($a, $b){
	global $sortfield;
	return strcmp($a[$sortfield], $b[$sortfield]);
}

function objcmpDesc($a, $b){
	global $sortfield;
	return strcmp($b[$sortfield], $a[$sortfield]);
}

function array_sort_by_field(&$array, $key, $asc=true){
	global $sortfield;
	$sortfield = $key;
	usort($array, $asc?"objcmpAsc":"objcmpDesc");
}

// function aoaObjcmpAsc($a, $b){
// 	global $sortfield;
// 	return strcmp($a[$sortfield], $b[$sortfield]);
// }

// function aoaObjcmpDesc($a, $b){
// 	global $sortfield;
// 	return strcmp($b[$sortfield], $a[$sortfield]);
// }

// function array_of_array_sort_by_field(&$array, $key, $asc=true){
// 	global $sortfield;
// 	$sortfield = $key;
// 	usort($array, $asc?"aoaObjcmpAsc":"aoaObjcmpDesc");
// }

/**
 *
 * returns current minuts since the Unix Epoch (January 1 1970 00:00:00 GMT)
 */
function nowMinuts(){
	return floor(time()/60);
}

function retarr($str){
	return array( 'result' => $str );
}


function curlGetContent($url){
	return curlGetContentId($url, '');
}

function curlGetContentId($url, $id){
	$ch = curl_init($url);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
	$html = curl_exec($ch);
	curl_close($ch);
	return $html;
}

function strEndsWith($haystack, $needle){
    $a = strlen($haystack);
    $b = strlen($needle);
    return (substr_compare($haystack, $needle, $a-$b, $b) === 0);
}

?>
