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
//

package org.dvijok.resources;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.md5;
import org.dvijok.rpc.DBObject;

public class AuthTool {

	private CustomEventTool authChangedET;
	private CustomEventTool loginET;
	private CustomEventTool logoutET;
	
	public AuthTool(){
		authChangedET = new CustomEventTool();
		loginET = new CustomEventTool();
		logoutET = new CustomEventTool();
	}
	
	public void logout(){
		logout(null);
	}
	
	public void logout( final RequestHandler<DBObject> onLogout ){
		
		Resources.getInstance().db.logout(null, new RequestHandler<DBObject>(){
	
			@Override
			public void success(DBObject result) {
				Resources.getInstance().onAuth(new DBObject());
				authChangedET.invokeListeners(Resources.getInstance().userData);
				logoutET.invokeListeners(Resources.getInstance().userData);
				if( onLogout != null ) onLogout.success(result);
			}
	
			@Override
			public void fail(DBObject result) {
				if( onLogout != null ) onLogout.fail(result);
			}});
	
	}
	
	private String login;
	private String pass;
	private String authkeychal;
	private CustomEventListener onLoginL;
	private CustomEventListener onLoginFailedL;

	public void login(String login, String pass){
		login(login, pass, null, null);
	}

	public void login(String login, String pass, CustomEventListener onLoginL){
		login(login, pass, onLoginL, null);
	}
	
	public void login(String login, String pass, CustomEventListener onLoginL, CustomEventListener onLoginFailedL){
		
		this.login = login;
		this.pass = pass;
		this.onLoginL = onLoginL;
		this.onLoginFailedL = onLoginFailedL;
		
		DBObject dbo = new DBObject();
		dbo.put("login", login);
		
		final RequestHandler<DBObject> loginrh = new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				onAuthSuccess(result);
			}

			@Override
			public void fail(DBObject result) {
				onLoginFailed(result, this);
			}
			
		};
		
		Resources.getInstance().db.login(dbo, loginrh);
	}
	
	private void onAuthSuccess(DBObject result){
		Resources.getInstance().userData = result.getDBObject("userdata");
		Resources.getInstance().onAuth(result.getDBObject("userinfo"));
		authChangedET.invokeListeners(Resources.getInstance().userData);
		loginET.invokeListeners(Resources.getInstance().userData);
		if( onLoginL != null ) onLoginL.customEventOccurred(new CustomEvent(Resources.getInstance().userData));
	}

//	private void sendAuthKey(){
//		
//		DBObject dbo = new DBObject();
//		dbo.put("login", login);
//		dbo.put("authkey", this.authkey.getValue());
//		dbo.put("response", md5.md5(this.authkeychal+pass.getValue()));
//
//		final RequestHandler<DBObject> authkeyrh = new RequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				onAuthSuccess(result);
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				onLoginFailed(result, this);
//			}
//			
//		};
//		
//		Resources.getInstance().db.sendKey(dbo, authkeyrh);
//	}
	
	private void onLoginFailed(DBObject result, RequestHandler<DBObject> loginrh){

		String res = result.getString("result");
		
		if( res.equals("challange") ){

			String resp = md5.md5(result.getString("chal")+pass);
			
			DBObject dbo = new DBObject();
			dbo.put("login", login);
			dbo.put("response", resp);
			
			Resources.getInstance().db.login( dbo, loginrh );
			
		} else if( res.equals("authkey") ){
			this.authkeychal = result.getDBObject("objects").getString("chal");
		} else {
			if( onLoginFailedL != null ) onLoginFailedL.customEventOccurred(new CustomEvent("login failed"));
		}
		
	}
	
	public void addAuthChangedListener(CustomEventListener listener){
		authChangedET.addCustomEventListener(listener);
	}
	
	public void removeAuthChangedListener(CustomEventListener listener){
		authChangedET.removeCustomEventListener(listener);
	}
	
	public void addLoginListener(CustomEventListener listener){
		loginET.addCustomEventListener(listener);
	}
	
	public void removeLoginListener(CustomEventListener listener){
		loginET.removeCustomEventListener(listener);
	}
	
	public void addLogoutListener(CustomEventListener listener){
		logoutET.addCustomEventListener(listener);
	}
	
	public void removeLogoutListener(CustomEventListener listener){
		logoutET.removeCustomEventListener(listener);
	}
	
	
}
