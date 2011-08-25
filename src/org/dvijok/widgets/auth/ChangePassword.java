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

import org.dvijok.db.DB_Object;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ChangePassword extends Sub_Panels_Dwidget {

	private Label message;
	private PasswordTextBox pass;
	private PasswordTextBox passconf;
	private Button dochangepass;
	private TextBox authkey;
	private Button sendauthkey;
	private String authkeychal;
	
	public ChangePassword(Sub_Panel p){
		super("tmpl/widgets/auth/change_password/change_password.html", p);
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		
		KeyDownHandler loginkdh = new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if( event.getNativeKeyCode() == KeyCodes.KEY_ENTER ){
					doChangePass();
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
		
		this.passconf = new PasswordTextBox();
		this.passconf.addKeyDownHandler(loginkdh);
		
		this.pass = new PasswordTextBox();
		this.pass.addKeyDownHandler(loginkdh);
		
		this.authkey = new TextBox();
		this.authkey.addKeyDownHandler(authkeykdh);
		
		this.sendauthkey = new Button("Отправить код");
		this.sendauthkey.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				sendAuthKey();
			}
		});
		this.sendauthkey.addKeyDownHandler(authkeykdh);
		
		this.dochangepass = new Button("Сменить пароль");
		this.dochangepass.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				doChangePass();
			}
			
		});
		this.dochangepass.addKeyDownHandler(loginkdh);
		
		message = new Label("");
	}
	
	private void onChPassSuccess(){
		Change_Tmpl("tmpl/widgets/auth/change_password/change_password.html");
		message.setText("Изменение пароля выполнено успешно");
		pass.setValue("");
		passconf.setValue("");
	}
	
	private void doChangePass(){
		
		String password = pass.getText();
		if( password.equals(passconf.getText()) ){
			
		DB_Object passdbo = new DB_Object();
		passdbo.put("password", password);
		DB_Object dbo = new DB_Object();
		dbo.put("dbid", "password");
		dbo.put("dbo", passdbo);
		
		final DV_Request_Handler<DB_Object> chpassrh = new DV_Request_Handler<DB_Object>(){

			@Override
			public void Success(DB_Object result) {
				onChPassSuccess();
			}

			@Override
			public void Fail(DB_Object result) {
				onChPassFailed(result, this);
			}
			
		};
		
		Resources.getInstance().db.Put_Object(dbo, chpassrh);
		
		} else {
			Lib.Alert("Подтверждение и пароль не совпадают");
			passconf.setFocus(true);
		}
	}
	
	private void sendAuthKey(){
		
		DB_Object dbo = new DB_Object();
		dbo.put("challange", authkeychal);
		dbo.put("responce", md5.md5(authkeychal+authkey.getText()));
		
		DB_Object reqdbo = new DB_Object();
		reqdbo.put("dbid", "seckey");
		reqdbo.put("dbo", dbo);

		final DV_Request_Handler<DB_Object> authkeyrh = new DV_Request_Handler<DB_Object>(){

			@Override
			public void Success(DB_Object result) {
				onChPassSuccess();
			}

			@Override
			public void Fail(DB_Object result) {
				//onChPassFailed(result, this);
				Lib.Alert("Неверно введен код");
				authkey.setFocus(true);
			}
			
		};
		
		Resources.getInstance().db.Put_Object(reqdbo, authkeyrh);
	}
	
	private void onChPassFailed(DB_Object result, DV_Request_Handler<DB_Object> loginrh){

		String res = result.Get_String("result");
		
		if( res.equals("key") ){
			this.authkeychal = result.Get_DB_Object("objects").Get_String("chal");
			Change_Tmpl("tmpl/widgets/auth/change_password/change_password_keymode.html");
		} else {
			message.setText("Ошибка: "+res);
			Change_Tmpl("tmpl/widgets/auth/change_password/change_password.html");
		}
		
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		if( dwname.equals("pass") ) return this.pass;
		else if( dwname.equals("passconf") ) return this.passconf;
		else if( dwname.equals("dochangepass") ) return this.dochangepass;
		else if( dwname.equals("authkey") ) return this.authkey;
		else if( dwname.equals("sendauthkey") ) return this.sendauthkey;
		else if( dwname.equals("message") ) return this.message;
		else return null;
	}
	
	
	
}
