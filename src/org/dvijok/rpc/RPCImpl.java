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

package org.dvijok.rpc;

import java.util.Date;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.handlers.Handler;
import org.dvijok.lib.Lib;
import org.dvijok.rpc.dvrpc.DVRPCProto;
import org.dvijok.rpc.event.DataBaseEventListener;
import org.dvijok.rpc.event.RPCEventsDB;
import org.dvijok.rpc.json.JSONProto;

public class RPCImpl implements RPC {

	private String sid;

	private RPCConfig config;
	private RPCRequestMaker rpcRequest;
	private RPCEventsDB rpce;
	private RPCRequest rpcEventsRequest;
	private CustomEventListener inited;
	
	public RPCImpl(RPCConfig config, CustomEventListener inited){
		this.config = config;
		rpcRequest = new RPCRequestMaker(config.getRpcUrl(), initProto());
		rpce = new RPCEventsDB(this);
		this.inited = inited;
		if( restoreSession() ) checkSession();
	}
	
	private RPCProto initProto(){
		if( config.getRpcType().equals("json") ) return new JSONProto();
		else return new DVRPCProto();
	}
	
	private void checkSession(){
		checkSession(new DBObject(),  new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				final DBObject checkSessionResult = result;
				
				getTemplatesCache(new RequestHandler<DBObject>(){

					@Override
					public void success(DBObject result) {
						checkSessionResult.put("tmplcache", result.get("tmplcache"));
						inited.customEventOccurred(new CustomEvent(checkSessionResult));
					}

					@Override
					public void fail(DBObject result) {
						inited.customEventOccurred(new CustomEvent(checkSessionResult));
					}});
				
			}

			@Override
			public void fail(DBObject result) {}});
	}
	
	protected void storeSession(){
		Date expTime = new Date(System.currentTimeMillis()+config.getSessionExpTime().getTime());
		com.google.gwt.user.client.Cookies.setCookie("dvijok.session", sid, expTime, Lib.getDomain(), "/", false);
	}
	
	//true if session is restored
	private boolean restoreSession(){
		sid = com.google.gwt.user.client.Cookies.getCookie("dvijok.session");
		if( sid == null ){
			initSession();
			return false;
		} else return true;
	}
	
	private void initSession(){
		initSession( new Handler<Boolean>(){
			@Override
			public void onHandle(Boolean param) {
				checkSession();
			}});
	}
	
	private void initSession(final Handler<Boolean> handler) {
		
		DBObject dbo = new DBObject();
		dbo.put("func", "initSession");
		
		rpcRequest.request(dbo, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				if( result.getString("result").equals("success") ){
//					Lib.alert("DataBaseDVRPC success: "+result);
					sid = result.getDBObject("objs").getString("sid");
					storeSession();
					if( handler != null ) handler.onHandle(true);
				}
//				else {
//					Lib.alert("DataBaseDVRPC: A: init session failed: "+result.getString("result"));
//				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBaseDVRPC: B: init session failed: "+result);
			}});
	}

	@Override
	public void login(final DBObject params, final RequestHandler<DBObject> handler) {

		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "login");
		req.put("obj", params);
		
		rpcRequest.request(req, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
					if( checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							login(params, handler);
						}}) ) handler.fail(result);
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: login: fail: "+result);
			}});
	}

	@Override
	public void sendLoginKey(DBObject params, RequestHandler<DBObject> handler) {
	}

	@Override
	public void logout(final DBObject params, final RequestHandler<DBObject> handler) {

		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "logout");
		
		rpcRequest.request(req, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(null);
				} else {
					if( checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							logout(params, handler);
						}}) ) handler.fail(result);
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: logout: fail: "+result);
			}});
	}

//	@Override
//	public void getObject(DBObject params, final DVRequestHandler<DBObject> handler) {
//		params.put("count", "1");
//		getObjects(params, new DVRequestHandler<DBArray>(){
//
//			@Override
//			public void success(DBArray retobjs) {
//				if( retobjs.size() > 0 ) handler.success(retobjs.getDBObject(0));
//				else handler.success(null);
//			}
//
//			@Override
//			public void fail(DBArray result) {
//			}
//			
//		});
//	}

//	@Override
//	public void getObjects(final DBObject params, final DVRequestHandler<DBArray> handler) {
//
//		DBObject req = new DBObject();
//		req.put("sid", sid);
//		req.put("func", "getObjects");
//		req.put("obj", params);
//		
//		dbRequest.request(req, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				String res = result.getString("result");
//				if( res.equals("success") ){
//					handler.success(result.getDBArray("objs"));
//				} else {
//					if( checkNotSid(res, new Handler<Boolean>(){
//						@Override
//						public void onHandle(Boolean param) {
//							getObjects(params, handler);
//						}}) ) Lib.alert("DataBase: getObjects A: fail: "+result);
//				}
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				Lib.alert("DataBase: getObjects B: fail: "+result);
//			}});
//	}

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

//	@Override
//	public void putObject(final DBObject params, final DVRequestHandler<DBObject> handler) {
//		DBObject req = new DBObject();
//		req.put("sid", sid);
//		req.put("func", "putObject");
//		req.put("obj", params);
//		
//		dbRequest.request(req, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				String res = result.getString("result");
//				if( res.equals("success") ){
//					handler.success(result.getDBObject("objs"));
//				} else {
//					if( checkNotSid(res, new Handler<Boolean>(){
//						@Override
//						public void onHandle(Boolean param) {
//							putObject(params, handler);
//						}}) ) Lib.alert("DataBase: putObject A: fail: "+result);
//				}
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				Lib.alert("DataBase: putObject B: fail: "+result);
//			}});
//	}

//	@Override
//	public void delObject(final DBObject params, final DVRequestHandler<DBObject> handler) {
//
//		DBObject req = new DBObject();
//		req.put("sid", sid);
//		req.put("func", "delObject");
//		req.put("obj", params);
//		
//		dbRequest.request(req, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				String res = result.getString("result");
//				if( res.equals("success") ){
//					handler.success(result.getDBObject("objs"));
//				} else {
//					if( checkNotSid(res, new Handler<Boolean>(){
//						@Override
//						public void onHandle(Boolean param) {
//							delObject(params, handler);
//						}}) ) Lib.alert("DataBase: delObject A: fail: "+result);
//				}
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				Lib.alert("DataBase: delObject B: fail: "+result);
//			}});
//	}

	@Override
	public void listenForEvents(final DBObject params, final RequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "listenForEvents");
		req.put("obj", params);
		
		rpcEventsRequest = rpcRequest.request(req, new RequestHandler<DBObject>(){

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
		if( rpcEventsRequest != null ) if( rpcEventsRequest.isPending() ) rpcEventsRequest.cancel();
	}
	
	public void pauseListenForEvents(){
		rpcEventsRequest.pause();
	}
	
	public void resumeListenForEvents(){
		rpcEventsRequest.resume();
	}

	@Override
	public void addEventListener(DBObject params, DataBaseEventListener listener) {
		rpce.addEventListener(params, listener);
	}

	@Override
	public void removeEventListener(DBObject params, DataBaseEventListener listener) {
		rpce.removeEventListener(params, listener);
	}

	@Override
	public void checkSession(final DBObject params, final RequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "checkSession");
		req.put("obj", params);
		
		rpcEventsRequest = rpcRequest.request(req, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
//					if(
					checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							checkSession(params, handler);
						}});
//					) Lib.alert("DataBase: resetEvents A: fail: ->"+result+"<-");
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: resetEvents B: fail: "+result);
			}});
	}

	@Override
	public void external(final DBObject params, final RequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "external");
		req.put("obj", params);
		
		rpcRequest.request(req, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
					if(
					checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							external(params, handler);
						}})
						)
					{
						handler.fail(result);
//						System.out.println("res: "+result);
//						Lib.alert("DataBase: external A: fail: "+result);
					}
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: external B: fail: "+result);
			}});
	}

	@Override
	public void saveUserData(final DBObject userData, final RequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "saveUserData");
		req.put("userdata", userData);
		
		rpcRequest.request(req, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
//					if(
					checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							saveUserData(userData, handler);
						}});
//					) {
//						System.out.println("res: "+result);
//						Lib.alert("DataBase: saveUserData A: fail: "+result);
//					}
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: saveUserData B: fail: "+result);
			}});
	}

	@Override
	public void getTemplatesCache(final RequestHandler<DBObject> handler) {
		DBObject req = new DBObject();
		req.put("sid", sid);
		req.put("func", "getTemplatesCache");
		
		rpcRequest.request(req, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				String res = result.getString("result");
				if( res.equals("success") ){
					handler.success(result.getDBObject("objs"));
				} else {
					checkNotSid(res, new Handler<Boolean>(){
						@Override
						public void onHandle(Boolean param) {
							getTemplatesCache(handler);
						}});
				}
			}

			@Override
			public void fail(DBObject result) {
				Lib.alert("DataBase: getTemplatesCache B: fail: "+result);
			}});
	}

	@Override
	public String getSid() {
		return sid;
	}
	
}
