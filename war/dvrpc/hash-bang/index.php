<?php

$page = $_GET["page"];
if( $page == '' ) $page='index.html';
$key = $_GET["key"];
$val = $_GET["val"];

#echo "$page : $key : $val <br/>";
require_once('loader.php');

global $state;
$state['page'] = $page;
$state['hashtoken'] = $key;

#require_once('resources.php');
$loader = new Loader();
echo $loader->load($page);

?>
