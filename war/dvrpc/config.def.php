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

$config['SESSION_EXPIRATION_TIME_ANON'] = 1440*7; //in minutes //1440 - day
$config['SESSION_EXPIRATION_TIME_AUTH_LONG'] = 1440*365; //if user checked 'remember me'
$config['SESSION_EXPIRATION_TIME_AUTH_SHORT'] = 5; //if not

#$config['CAPTCHA_FAIL'] = 3; // number of wrong passwords before captcha
#$config['CAPTCHA_TIME'] = 10; // time in sec between wrong passwords
#$config['CAPTCHA_EXPIRE'] = 10000; // time in sec captcha will not use after
#$config['CAPTCHA_URL'] = 'http://dvijok/xmlrpcdb/captcha.php';
#$config['CAPTCHA_SOUND_URL'] = 'http://dvijok/xmlrpcdb/captchasound.php';

#$config['shmemkey'] = 100;

//vkontakte
$config['appid'] = '2977906';
$config['appsecret'] = '7PrS5eoa08XuZFuUitrj';

$config['ipcfilesdir'] = dirname(__FILE__).'/ipcfiles/';
$config['ipcsystimeout'] = 600;//in seconds
$config['ipcusertimeout'] = 600;//in seconds

$config['dbtype'] = 'dbfiles';
$config['dbfilesdir'] = dirname(__FILE__).'/dbfiles/';

#$config['dbtype'] = 'mysql';
#$config['MYSQLHOST'] = "localhost";
#$config['MYSQLUSER'] = "dvijok";
#$config['MYSQLPASS'] = "dvijok";
#$config['MYSQLDBNAME'] = "dvijok";

date_default_timezone_set('Asia/Yekaterinburg');

if( file_exists('../custom/config/config.php') ) require_once '../custom/config/config.php';

//moderator vk ids
$config['moderatorVkIds'] = array('38844032', '3243536', '3050005');

//send to error_log called functions through rpc
$config['debugrpcfuncs'] = false;

//modules
require_once 'modules/vk.php';

?>
