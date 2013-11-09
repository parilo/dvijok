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

require_once 'lib.php';
require_once 'config.def.php';

class vk {

	//set auth requested by the user
	public function setAuthReq($inp, $user, $sess, $db){
		$sess['userdata']['vkauthreq'] = '1';
#		$db->getDB()->putObject($sess, false);
		$db->saveSession($sess);
		$ret['result'] = 'success';
		return $ret;
	}
	
	//if auth through vk was requested by this user
	public function isAuthReq($inp, $user, $sess, $db){
		$userdata = $db->getSessionUserData($sess['sid']);
		$userdata = $userdata['userdata'];

		$isreq = '0';

		if( isset($userdata['vkauthreq']) )
		if( $userdata['vkauthreq'] == '1' ) $isreq = '1';
		
		$ret['objs']['isreq'] = $isreq;

		$ret['result'] = 'success';
		return $ret;
	}
	
	/* not used now - servser side authorization */
	public function authCode($inp, $user, $sess, $db){
		$code = $inp['obj']['params']['code'];
		if( is_string($code) ) /*if( strlen($code) == 67 )*/ {
			global $config;
			$acctokenurl = "https://oauth.vk.com/access_token?client_id=".$config['appid']."&client_secret=".$config['appsecret']."&code=".$code;
			
			$resp = curlGetContent($acctokenurl);
// 			$resp = '{"access_token":"533bacf01e11f55b536a565b57531ac114461ae8736d6506a3", "expires_in":43200, "user_id":6492}';
			$resp = json_decode($resp, true);
			
			if( isset($resp['error']) )
			if( $resp['error'] === 'invalid_grant' ){
				$ret['objs']['text'] = 'vkauth: '.$resp['error_description'];
				$ret['result'] = 'fail';
				return $ret;
			}
			
			if( isset($resp['access_token']) && isset($resp['user_id']) ){
				
				//getting info from http://vk.com/developers.php?oid=-1&p=users.get
				$infourl = 'https://api.vk.com/method/users.get?uids='.$resp['user_id'].'&fields=first_name,last_name,photo_rec&access_token='.$resp['access_token'];
				$inforesp = curlGetContent($infourl);
// 				$inforesp = '{"response":[{"uid":"1","first_name":"Павел","last_name":"Дуров","photo_rec":"http:\/\/cs109.vkontakte.ru\/u00001\/c_df2abf56.jpg"}]}';
				$inforesp = json_decode($inforesp, true);
				$inforesp = $inforesp['response'][0];
				
				if( isset($inforesp['first_name']) && isset($inforesp['last_name']) && isset($inforesp['photo_rec']) ){

					$vkid = $inforesp['uid'];
					$uid = 'vk'.$vkid;

// 					$checkuser = $db->getDB()->getObjectByTags('user '.$uid/*, $root*/);
					$checkuser = $db->getUser($uid);
					if( $checkuser === false ){
						//if user with such uid not exists - create user based on $user argument
						if( isset($user['id']) ) unset($user['id']);
						$user['uid'] = $uid;
					} else {
						$user = $checkuser;
					}
					
					$user['userinfo']['type'] = 'vk';
// 					$user['userinfo']['id'] = $uid;
					$user['userinfo']['profile.id'] = 'vk'.$vkid;
					$user['userinfo']['profile.url'] = 'http://vk.com/id'.$vkid;
					$user['userinfo']['picture'] = $inforesp['photo_rec'];
					$user['userinfo']['name'] = $inforesp['first_name'].' '.$inforesp['last_name'];
					if( in_array($vkid, $config['moderatorVkIds']) ) $user['userinfo']['ismoderator'] = '1';
					else $user['userinfo']['ismoderator'] = '0';
					
					$uitypes['type'] = 'str';
					$uitypes['picture'] = 'str';
					$uitypes['name'] = 'str';
					$uitypes['ismoderator'] = 'int';
					$uitypes['profile.url'] = 'str';
					$uitypes['profile.id'] = 'str';
										
//  					$db->getDB()->putObject($user, 'user '.$uid/*, $root*/);
					$user['uid'] = $uid;
					$user['nologin'] = '1';
					$db->saveUser($user);
					$db->saveUserData($uid, $user['userinfo'], $uitypes);
 					
 					$sess['uid'] = $uid;
 					$sess['authed'] = '1';
					$sess['userdata']['vkauthreq'] = '0';
					//if( isset($sess['vkauthreq']) ) unset($sess['vkauthreq']);
//					$db->getDB()->putObject($sess, 'sess auth'/*, $root*/);
 					$db->saveSession($sess);
 					
					$ret['objs']['userinfo'] = $user['userinfo'];
					$ret['result'] = 'success';
					return $ret;
					
				} else {
					$ret['objs']['text'] = 'vkauth: error 2';
					$ret['result'] = 'fail';
					return $ret;
				}
				
			} else {
				$ret['objs']['text'] = 'vkauth: error 1';
				$ret['result'] = 'fail';
				return $ret;
			}
			
		} else {
			$ret['result'] = 'fail';
		}
		return $ret;
	}
	
	/* used now - client-side authorization */
	public function authAccessToken($inp, $user, $sess, $db){
		$acctoken = $inp['obj']['params']['accesstoken'];
		$vkuserid = $inp['obj']['params']['userid'];
		if( is_string($acctoken) && is_string($vkuserid) ) {
			global $config;
				
			//getting info from http://vk.com/developers.php?oid=-1&p=users.get
			$infourl = 'https://api.vk.com/method/users.get?uids='.$vkuserid.'&fields=first_name,last_name,photo_rec&access_token='.$acctoken;
			$inforesp = curlGetContent($infourl);
// 			$inforesp = '{"response":[{"uid":"1","first_name":"Павел","last_name":"Дуров","photo_rec":"http:\/\/cs109.vkontakte.ru\/u00001\/c_df2abf56.jpg"}]}';
			$inforesp = json_decode($inforesp, true);
			$inforesp = $inforesp['response'][0];

			if( isset($inforesp['first_name']) && isset($inforesp['last_name']) && isset($inforesp['photo_rec']) ){

				$vkid = $inforesp['uid'];
				$uid = 'vk'.$vkid;

				$checkuser = $db->getDB()->getObjectByTags('user '.$uid/*, $root*/);
				if( $checkuser === false ){
					//if user with such uid not exists - create user based on $user argument
					if( isset($user['id']) ) unset($user['id']);
					$user['uid'] = $uid;
				} else {
					$user = $checkuser;
				}
					
				$user['userinfo']['type'] = 'vk';
				$user['userinfo']['id'] = $uid;
				$user['userinfo']['profile.url'] = 'http://vk.com/id'.$vkid;
				$user['userinfo']['picture'] = $inforesp['photo_rec'];
				$user['userinfo']['name'] = $inforesp['first_name'].' '.$inforesp['last_name'];
				if( in_array($vkid, $config['moderatorVkIds']) ) $user['userinfo']['ismoderator'] = '1';
				else if( isset($user['userinfo']['ismoderator']) ) unset($user['userinfo']['ismoderator']);
				
				$db->getDB()->putObject($user, 'user '.$uid/*, $root*/);

				$sess['uid'] = $uid;
				if( isset($sess['vkauthreq']) ) unset($sess['vkauthreq']);
				$db->getDB()->putObject($sess, 'sess auth'/*, $root*/);

				$ret['objs']['userinfo'] = $user['userinfo'];
				$ret['result'] = 'success';
				return $ret;
					
			} else {
				$ret['objs']['text'] = 'vkauth: error 2';
				$ret['result'] = 'fail';
				return $ret;
			}
				
		} else {
			$ret['result'] = 'fail';
		}
		return $ret;
	}
	

}

?>
