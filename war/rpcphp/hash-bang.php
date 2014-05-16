<?php

#$incl = get_include_path();
#set_include_path($incl . PATH_SEPARATOR . './site/dvrpc/');

require_once 'config.php';
#require_once 'external/estate.php';
require_once "db/db.php";
require_once "db/dbinit.php";
require_once 'config.def.php';
require_once 'config.php';
require_once 'dbget.php';
global $config;
#$db = new DataBase($config['dbfilesdir']);
$db = getDB();
#if( !$db->isInitialized() ){
#    $dbinit = new DataBaseInit();
#    $dbinit->init($db);
#}

global $state;
$state['db'] = $db;
$state['page'] = $_GET['page'];
$state['key'] = $_GET['key'];
$state['val'] = $_GET['val'];

#$estate = new estate();
#$state['estate'] = $estate;

$sess['sid'] = 'hash-bang';
$sess['uid'] = 'guest';
$sess['ip'] = '127.0.0.1';
$state['sess'] = $sess;

$state['user'] = $db->getUser('guest');//getObjectByTags('user guest');

// try{

require_once 'hash-bang/index.php';

// } catch ( DBException $ex ){
// 	$ret['result'] = '->'.$ex->getValue().'<- '.$ex->getTraceAsString();
// 	echo print_r($ret, true);
// }

#set_include_path($incl);

?>
