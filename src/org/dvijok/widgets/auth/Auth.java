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

package org.dvijok.widgets.auth;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.lib.md5;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Auth extends SubPanelsDwidget {

	private TextBox login;
	private PasswordTextBox pass;
	private Button dologin;
	private TextBox authkey;
	private Button sendauthkey;
	private String authkeychal;
	
	public Auth(SubPanel p){
		super("tmpl/widgets/auth/auth/auth.html", p);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		
		KeyDownHandler loginkdh = new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ){
					doLogin();
				}
			}
		};
		
		KeyDownHandler authkeykdh = new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ){
					sendAuthKey();
				}
			}
		};
		
		this.login = new TextBox();
		this.login.addKeyDownHandler(loginkdh);
		
		this.pass = new PasswordTextBox();
		this.pass.addKeyDownHandler(loginkdh);
		
		this.authkey = new TextBox();
		this.authkey.addKeyDownHandler(authkeykdh);
		
		this.sendauthkey = new Button();
		this.sendauthkey.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				sendAuthKey();
			}
		});
		this.sendauthkey.addKeyDownHandler(authkeykdh);
		
		this.dologin = new Button();
		this.dologin.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				doLogin();
			}
			
		});
		this.dologin.addKeyDownHandler(loginkdh);
	}
	
	private void onAuthSuccess(){
		Resources.getInstance().getUserInfo(new DVRequestHandler<Integer>(){

			@Override
			public void success(Integer result) {
				Resources.getInstance().dwidgets.authReload();
			}

			@Override
			public void fail(Integer result) {}
		});
	}
	
	private void doLogin(){
		DBObject dbo = new DBObject();
		dbo.put("login", login.getValue());
		
		final DVRequestHandler<DBObject> loginrh = new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				onAuthSuccess();
			}

			@Override
			public void fail(DBObject result) {
				onLoginFailed(result, this);
			}
			
		};
		
		Resources.getInstance().db.auth(dbo, loginrh);
	}
	
	private void sendAuthKey(){
		
		DBObject dbo = new DBObject();
		dbo.put("login", login.getValue());
		dbo.put("authkey", this.authkey.getValue());
		dbo.put("response", md5.md5(this.authkeychal+pass.getValue()));

		final DVRequestHandler<DBObject> authkeyrh = new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				onAuthSuccess();
			}

			@Override
			public void fail(DBObject result) {
				onLoginFailed(result, this);
			}
			
		};
		
		Resources.getInstance().db.sendKey(dbo, authkeyrh);
	}
	
	private void onLoginFailed(DBObject result, DVRequestHandler<DBObject> loginrh){

		String res = result.getString("result");
		
		if( res.equals("challange") ){

			String resp = md5.md5(result.getDBObject("objects").getString("chal")+pass.getValue());
			
			DBObject dbo = new DBObject();
			dbo.put("login", login.getValue());
			dbo.put("response", resp);
			
			Resources.getInstance().db.auth( dbo, loginrh );
			
		} else if( res.equals("authkey") ){
			this.authkeychal = result.getDBObject("objects").getString("chal");
			changeTmpl("tmpl/widgets/auth/auth/authkeymode.html");
		} else {
			changeTmpl("tmpl/widgets/auth/auth/auth.html");
			Lib.alert("Неверный логин или пароль");
			pass.setFocus(true);
		}
		
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("login") ) return this.login;
		else if( dwname.equals("pass") ) return this.pass;
		else if( dwname.equals("dologin") ){
			if( params != null ){
				this.dologin.setText(params.get(0).getString("LABEL"));
			}
			return this.dologin;
		}
		else if( dwname.equals("authkey") ) return this.authkey;
		else if( dwname.equals("sendauthkey") ){
			if( params != null ){
				this.sendauthkey.setText(params.get(0).getString("LABEL"));
			}
			return this.sendauthkey;
		}
		else return null;
	}
	
	
	
}
