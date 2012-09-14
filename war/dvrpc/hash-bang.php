<?php

#$incl = get_include_path();
#set_include_path($incl . PATH_SEPARATOR . './site/dvrpc/');

require_once 'dvobjfilter.php';
require_once 'external/estate.php';
require_once "db/db.php";
require_once "db/dbinit.php";
require_once 'config.def.php';
require_once 'config.php';
global $config;
$db = new DataBase($config['dbfilesdir']);
if( !$db->isInitialized() ){
    $dbinit = new DataBaseInit();
    $dbinit->init($db);
}

global $state;
$state['db'] = $db;

$estate = new estate();
$state['estate'] = $estate;

$sess['sid'] = 'hash-bang';
$sess['uid'] = 'guest';
$sess['ip'] = '127.0.0.1';
$state['sess'] = $sess;

$state['user'] = $db->getObjectByTags('user guest');

require_once 'hash-bang/index.php';

#set_include_path($incl);

?>
