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

package org.dvijok.db;

import java.util.Date;

import org.dvijok.db.dvrpc.DBRequestMakerDVRPC;
import org.dvijok.db.event.DataBaseEventListener;
import org.dvijok.db.event.DataBaseEventsDB;
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.handlers.Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.Window.ScrollEvent;
import com.google.gwt.user.client.Window.ScrollHandler;
import com.sun.java.swing.plaf.windows.WindowsBorders;

public class DataBaseImpl implements DataBase {

	private DBRequestMaker dbRequest;
	private String sid;
	private DataBaseEventsDB dbe;
	private DBRequest dbEventsRequest;
	
	public DataBaseImpl(){
		dbRequest = new DBRequestMakerDVRPC(Resources.getInstance().conf.dbUrl);
		dbe = new DataBaseEventsDB(this);
		restoreSession();
		
//		Window.addResizeHandler(new ResizeHandler(){
//
//			@Override
//			public void onResize(ResizeEvent event) {
//				Lib.alert("resize 1");
//			}});
//		
//		com.google.gwt.user.client.Window.addWindowScrollHandler(new ScrollHandler(){
//
//			@Override
//			public void onWindowScroll(ScrollEvent event) {
//				Lib.alert("scroll");
//				
//			}
//			
//		});
//		
//		com.google.gwt.user.client.Window.addCloseHandler(new CloseHandler<Window>(){
//
//			@Override
//			public void onClose(CloseEvent<Window> event) {
//				Lib.alert("close 1");
//			}});
//		
//		com.google.gwt.user.client.Window.addWindowClosingHandler(new ClosingHandler(){
//
//			@Override
//			public void onWindowClosing(ClosingEvent event) {
//				event.setMessage("aaaaa");
//				Lib.alert("close 2");
//			}});
		
		new DataBaseTest(this);
	}
	
	protected void storeSession(){
		Date expTime = new Date(System.currentTimeMillis()+Resources.getInstance().conf.sessExpTime.getTime());
		com.google.gwt.user.client.Cookies.setCookie("dvijok.session", sid, expTime, Lib.getDomain(), "/", false);
	}
	
	private void restoreSession(){
		sid = com.google.gwt.user.client.Cookies.getCookie("dvijok.session");
		if( sid == null ) initSession();
	}
	
	private void initSession(){
		initSession(null);
	}
	
	private void initSession(final Handler<Boolean> handler) {
		
		DBObject dbo = new DBObject();
		dbo.put("func", "initSession");
		
		dbRequest.request(dbo, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				if( result.getString("result").equals("success") ){
//					Lib.alert("DataBaseDVRPC success: "+result);
					sid = result.getDBObject("objs").getString("sid");
					storeSession();
					if( handler != null ) handler.onHandle(true);
				} else {
					Lib.alert("DataBaseDVRPC: A: init session failed: "+result.getString("result"));
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBaseDVRPC: B: init session failed: "+result);
			}});
	}

	@Override
	public void auth(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void sendKey(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void logout(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void getObject(DBObject params, final DVRequestHandler<DBObject> handler) {
		params.put("count", "1");
		getObjects(params, new DVRequestHandler<DBArray>(){

			@Override
			public void success(DBArray retobjs) {
				if( retobjs.size() > 0 ) handler.success(retobjs.getDBObject(0));
				else handler.success(null);
			}

			@Override
			public void fail(DBArray result) {
			}
			
		});
	}

	@Override
	public void getObjects(final DBObject params, final DVRequestHandler<DBArray> handler) {

		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "getObjects");
		req.put("obj", params);
		
		dbRequest.request(req, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBArray("objs"));
				} else {
					if( checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							getObjects(params, handler);
						}}) ) Lib.alert("DataBase: getObjects A: fail: "+result);
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: getObjects B: fail: "+result);
			}});
	}

	/**
	 * Checks if result is 'notsid' and if so, init new Session and invoke handler after it
	 * @param result
	 * @param handler
	 * @return true if result is not 'notsid'
	 */
	private boolean checkNotSid(String result, Handler<Boolean> handler){
		if( result.equals("notsid") ){
			initSession(handler);
			return false;
		} else return true;
	}

	@Override
	public void putObject(final DBObject params, final DVRequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "putObject");
		req.put("obj", params);
		
		dbRequest.request(req, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
					if( checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							putObject(params, handler);
						}}) ) Lib.alert("DataBase: putObject A: fail: "+result);
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: putObject B: fail: "+result);
			}});
	}

	@Override
	public void delObject(final DBObject params, final DVRequestHandler<DBObject> handler) {

		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "delObject");
		req.put("obj", params);
		
		dbRequest.request(req, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
					if( checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							delObject(params, handler);
						}}) ) Lib.alert("DataBase: delObject A: fail: "+result);
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: delObject B: fail: "+result);
			}});
	}

	@Override
	public void listenForEvents(final DBObject params, final DVRequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "listenForEvents");
		req.put("obj", params);
		
		dbEventsRequest = dbRequest.request(req, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
					if( checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							listenForEvents(params, handler);
						}}) ) /*Lib.alert("DataBase: listenForEvents A: fail: ->"+result+"<-")*/;
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: listenForEvents B: fail: "+result);
			}});
	}

	public void stopListenForEvents(){
		if( dbEventsRequest != null ) if( dbEventsRequest.isPending() ) dbEventsRequest.cancel();
	}
	
	public void pauseListenForEvents(){
		dbEventsRequest.pause();
	}
	
	public void resumeListenForEvents(){
		dbEventsRequest.resume();
	}

	@Override
	public void addEventListener(DBObject params, DataBaseEventListener listener) {
		dbe.addEventListener(params, listener);
	}

	@Override
	public void removeEventListener(DBObject params, DataBaseEventListener listener) {
		dbe.removeEventListener(params, listener);
	}
	
}
