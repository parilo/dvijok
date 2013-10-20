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

package org.dvijok.widgets.auth.socauth;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.lib.md5;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;
import org.dvijok.widgets.auth.AuthCancelable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class VkAuth extends SubPanelsDwidget implements AuthCancelable {

	private Image vk;
	private Button cancel;
	private CustomEventTool loginedET;
	private CustomEventTool cancelET;
	
	public VkAuth(){
		super("tmpl/widgets/auth/socauth/vkauth.html");
	}
	
	public VkAuth(SubPanel p){
		super("tmpl/widgets/auth/socauth/vkauth.html", p);
	}
	
	public void addLoginedListener(CustomEventListener listener){
		loginedET.addCustomEventListener(listener);
	}
	
	public void removeLoginedListener(CustomEventListener listener){
		loginedET.removeCustomEventListener(listener);
	}

//	@Override
//	public boolean needAuthReinit() {
//		return true;
//	}

	@Override
	public void addCancelListener(CustomEventListener listener) {
		cancelET.addCustomEventListener(listener);
	}

	@Override
	public void removeCancelListener(CustomEventListener listener) {
		cancelET.removeCustomEventListener(listener);
	}
	
	private void testredirect(String str){
		Lib.redirect("http://127.0.0.1:8888/index.html?gwt.codesvr=127.0.0.1:9997&code=7a6fa4dff77a228eeda56603b8f53806c883f011c40b72630bb50df056f6479e52a");
//		Lib.redirect("http://127.0.0.1:8888/index.html?gwt.codesvr=127.0.0.1:9997#access_token=533bacf01e11f55b536a565b57531ad114461ae8736d6506a3&expires_in=86400&user_id=8492");
	}
	
	private void doLogin(){
		
		DBObject dbo = new DBObject();
		dbo.put("module", "vk");
		dbo.put("func", "setAuthReq");
		
		Resources.getInstance().db.external(dbo, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				//server-auth
				String redir = "http://oauth.vk.com/authorize?client_id="+Resources.getInstance().conf.appid+
						"&scope="+Resources.getInstance().conf.settings+
						"&redirect_uri="+Lib.getUrlWithoutParameters()+
						"&response_type=code";
				
//client-auth
//				String redir = "http://oauth.vk.com/authorize?client_id="+Resources.getInstance().conf.appid+
//						"&scope="+Resources.getInstance().conf.settings+
//						"&redirect_uri="+Lib.getUrlWithoutParameters()+
//						"&display=page"+
//						"&response_type=token";
				
//				testredirect(redir);
				Lib.redirect(redir);
			}

			@Override
			public void fail(DBObject result) {
			}});
		
	}
	
	private void doAuthWithCode(String code){
		//we got code from vk need continiue authorization
		DBObject params = new DBObject();
		params.put("code", code);
		
		DBObject dbo = new DBObject();
		dbo.put("module", "vk");
		dbo.put("func", "authCode");
		dbo.put("params", params);
		
		Resources.getInstance().db.external(dbo, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				if( result.containsKey("userinfo") ){
//					Resources.getInstance().onAuth(result.getDBObject("userinfo"));
					Lib.redirect(Lib.getUrlWithoutParameters());
				}
			}

			@Override
			public void fail(DBObject result) {
			}});
	}
	
	private void doAuthWithAccessToken(String accessToken, String userId){
		//we got code from vk need continiue authorization
		DBObject params = new DBObject();
		params.put("accesstoken", accessToken);
		params.put("userid", userId);
		
		DBObject dbo = new DBObject();
		dbo.put("module", "vk");
		dbo.put("func", "authAccessToken");
		dbo.put("params", params);
		
		Resources.getInstance().db.external(dbo, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				if( result.containsKey("userinfo") ){
					Lib.changeHashToken("", false);
					Resources.getInstance().onAuth(result.getDBObject("userinfo"));
//					Lib.redirect(Lib.getUrlWithoutParameters());
				}
			}

			@Override
			public void fail(DBObject result) {
			}});
	}
	
	private void checkCode(){

//client-auth		
//		String accessToken = Lib.getUrlHashParameter("access_token");
//		String userId = Lib.getUrlHashParameter("user_id");
//		
//		if( accessToken != null && userId != null ){
//			doAuthWithAccessToken(accessToken, userId);
//		}
		
		final String code = Lib.getUrlParameter("code");
		if( code != null && !Resources.getInstance().isAuthorized()) /*if( code.length() == 67 )*/ {
		
			DBObject dbo = new DBObject();
			dbo.put("module", "vk");
			dbo.put("func", "isAuthReq");
			
			Resources.getInstance().db.external(dbo, new RequestHandler<DBObject>(){
	
				@Override
				public void success(DBObject result) {
					if( result.getString("isreq").equals("1") ){
						doAuthWithCode(code);
					}
				}
	
				@Override
				public void fail(DBObject result) {
				}});
		}
	}

	@Override
	protected void initInternals() {
		vk = new Image("/images/social/vk32.png");
		vk.setTitle("Вконтакте");
		vk.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				doLogin();
			}
		});

		cancel = new Button("Отмена");
		cancel.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				cancelET.invokeListeners();
			}
		});
		
		checkCode();
		
		loginedET = new CustomEventTool();
		cancelET = new CustomEventTool();
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("vk") ) return vk;
		else if( dwname.equals("cancel") ) return cancel;
		return null;
	}

	
}
