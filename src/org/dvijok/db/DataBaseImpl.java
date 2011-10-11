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

import org.dvijok.db.dvrpc.DBRequestDVRPC;
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.handlers.Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

public class DataBaseImpl implements DataBase {

	private DBRequest dbRequest;
	private String sid;
	
	public DataBaseImpl(){
		dbRequest = new DBRequestDVRPC(Resources.getInstance().conf.dbUrl);
		restoreSession();
		
//putObject test
//		
//		DBObject dbo = new DBObject();
//		dbo.put("dddd", "4444");
//		dbo.put("ffff", "5555");
//		
//		DBObject mdbo = new DBObject();
//		mdbo.put("tags", "tag1 tag2");
//		mdbo.put("dbo", dbo);
//		
//		putObject(mdbo, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				Lib.alert("ret: "+result);
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				// TODO Auto-generated method stub
//				
//			}});

//getObject test
//
//		DBObject dbo = new DBObject();
//		dbo.put("tags", "tag1");
//		dbo.put("offset", "1");
//		
//		getObject(dbo, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				Lib.alert("ret: "+result);
//			}
//
//			@Override
//			public void fail(DBObject result) {}});

//getObjects test
//		
//		DBObject dbo = new DBObject();
//		dbo.put("tags", "tag1");
//		dbo.put("offset", "4");
//		dbo.put("count", "2");
//		
//		getObjects(dbo, new DVRequestHandler<DBArray>(){
//
//			@Override
//			public void success(DBArray result) {
//				Lib.alert("ret: "+result);
//			}
//
//			@Override
//			public void fail(DBArray result) {}});
	
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
	public void delObject(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void listenForEvents(DBObject params, DVRequestHandler<DBObject> handler) {
		DBObject dbo = new DBObject();
		dbo.put("func", "listenForEvent");
		dbo.put("aaa", "1234");
		dbo.put("bbbbb", "123");
		
		dbRequest.request(dbo, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				Lib.alert("DataBaseDVRPC success: "+result);
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBaseDVRPC fail: "+result);
			}});
	}

}
