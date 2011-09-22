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
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.lib.md5;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Auth extends Sub_Panels_Dwidget {

	private TextBox login;
	private PasswordTextBox pass;
	private Button dologin;
	private TextBox authkey;
	private Button sendauthkey;
	private String authkeychal;
	
	public Auth(Sub_Panel p){
		super("tmpl/widgets/auth/auth/auth.html", p);
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		
		KeyDownHandler loginkdh = new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ){
					Do_Login();
				}
			}
		};
		
		KeyDownHandler authkeykdh = new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ){
					Send_AuthKey();
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
				Send_AuthKey();
			}
		});
		this.sendauthkey.addKeyDownHandler(authkeykdh);
		
		this.dologin = new Button();
		this.dologin.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				Do_Login();
			}
			
		});
		this.dologin.addKeyDownHandler(loginkdh);
	}
	
	private void On_Auth_Success(){
		Resources.getInstance().Get_User_Info(new DV_Request_Handler<Integer>(){

			@Override
			public void Success(Integer result) {
				Resources.getInstance().dwidgets.Auth_Reload();
			}

			@Override
			public void Fail(Integer result) {}
		});
	}
	
	private void Do_Login(){
		DBObject dbo = new DBObject();
		dbo.put("login", login.getValue());
		
		final DV_Request_Handler<DBObject> loginrh = new DV_Request_Handler<DBObject>(){

			@Override
			public void Success(DBObject result) {
				On_Auth_Success();
			}

			@Override
			public void Fail(DBObject result) {
				On_Login_Failed(result, this);
			}
			
		};
		
		Resources.getInstance().db.Auth(dbo, loginrh);
	}
	
	private void Send_AuthKey(){
		
		DBObject dbo = new DBObject();
		dbo.put("login", login.getValue());
		dbo.put("authkey", this.authkey.getValue());
		dbo.put("response", md5.md5(this.authkeychal+pass.getValue()));

		final DV_Request_Handler<DBObject> authkeyrh = new DV_Request_Handler<DBObject>(){

			@Override
			public void Success(DBObject result) {
				On_Auth_Success();
			}

			@Override
			public void Fail(DBObject result) {
				On_Login_Failed(result, this);
			}
			
		};
		
		Resources.getInstance().db.Send_Key(dbo, authkeyrh);
	}
	
	private void On_Login_Failed(DBObject result, DV_Request_Handler<DBObject> loginrh){

		String res = result.Get_String("result");
		
		if( res.equals("challange") ){

			String resp = md5.md5(result.Get_DB_Object("objects").Get_String("chal")+pass.getValue());
			
			DBObject dbo = new DBObject();
			dbo.put("login", login.getValue());
			dbo.put("response", resp);
			
			Resources.getInstance().db.Auth( dbo, loginrh );
			
		} else if( res.equals("authkey") ){
			this.authkeychal = result.Get_DB_Object("objects").Get_String("chal");
			Change_Tmpl("tmpl/widgets/auth/auth/authkeymode.html");
		} else {
			Change_Tmpl("tmpl/widgets/auth/auth/auth.html");
			Lib.Alert("Неверный логин или пароль");
			pass.setFocus(true);
		}
		
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("login") ) return this.login;
		else if( dwname.equals("pass") ) return this.pass;
		else if( dwname.equals("dologin") ){
			if( params != null ){
				this.dologin.setText(params.get(0).Get_String("LABEL"));
			}
			return this.dologin;
		}
		else if( dwname.equals("authkey") ) return this.authkey;
		else if( dwname.equals("sendauthkey") ){
			if( params != null ){
				this.sendauthkey.setText(params.get(0).Get_String("LABEL"));
			}
			return this.sendauthkey;
		}
		else return null;
	}
	
	
	
}
