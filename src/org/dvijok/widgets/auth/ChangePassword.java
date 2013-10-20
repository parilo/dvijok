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
import org.dvijok.handlers.RequestHandler;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ChangePassword extends SubPanelsDwidget {

	private Label message;
	private PasswordTextBox pass;
	private PasswordTextBox passconf;
	private Button dochangepass;
	private TextBox authkey;
	private Button sendauthkey;
	private String authkeychal;
	
	public ChangePassword(SubPanel p){
		super("tmpl/widgets/auth/change_password/change_password.html", p);
	}

//	@Override
//	public boolean needAuthReinit() {
//		return true;
//	}

	@Override
	protected void beforeSubPanelsLoading() {
		
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
		changeTmpl("tmpl/widgets/auth/change_password/change_password.html");
		message.setText("Изменение пароля выполнено успешно");
		pass.setValue("");
		passconf.setValue("");
	}
	
	private void doChangePass(){
		
		String password = pass.getText();
		if( password.equals(passconf.getText()) ){
			
//		DBObject passdbo = new DBObject();
//		passdbo.put("password", password);
//		DBObject dbo = new DBObject();
//		dbo.put("dbid", "password");
//		dbo.put("dbo", passdbo);
//		
//		final DVRequestHandler<DBObject> chpassrh = new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				onChPassSuccess();
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				onChPassFailed(result, this);
//			}
//			
//		};
//		
//		Resources.getInstance().db.putObject(dbo, chpassrh);
		
		} else {
			Lib.alert("Подтверждение и пароль не совпадают");
			passconf.setFocus(true);
		}
	}
	
	private void sendAuthKey(){
		
		DBObject dbo = new DBObject();
		dbo.put("challange", authkeychal);
		dbo.put("responce", md5.md5(authkeychal+authkey.getText()));
		
		DBObject reqdbo = new DBObject();
		reqdbo.put("dbid", "seckey");
		reqdbo.put("dbo", dbo);

		final RequestHandler<DBObject> authkeyrh = new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				onChPassSuccess();
			}

			@Override
			public void fail(DBObject result) {
				//onChPassFailed(result, this);
				Lib.alert("Неверно введен код");
				authkey.setFocus(true);
			}
			
		};
		
//		Resources.getInstance().db.putObject(reqdbo, authkeyrh);
	}
	
	private void onChPassFailed(DBObject result, RequestHandler<DBObject> loginrh){

		String res = result.getString("result");
		
		if( res.equals("key") ){
			this.authkeychal = result.getDBObject("objects").getString("chal");
			changeTmpl("tmpl/widgets/auth/change_password/change_password_keymode.html");
		} else {
			message.setText("Ошибка: "+res);
			changeTmpl("tmpl/widgets/auth/change_password/change_password.html");
		}
		
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("pass") ) return this.pass;
		else if( dwname.equals("passconf") ) return this.passconf;
		else if( dwname.equals("dochangepass") ) return this.dochangepass;
		else if( dwname.equals("authkey") ) return this.authkey;
		else if( dwname.equals("sendauthkey") ) return this.sendauthkey;
		else if( dwname.equals("message") ) return this.message;
		else return null;
	}
	
	
	
}
