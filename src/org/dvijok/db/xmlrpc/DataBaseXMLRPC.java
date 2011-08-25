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

package org.dvijok.db.xmlrpc;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.fredhat.gwt.xmlrpc.client.XmlRpcClient;
import com.fredhat.gwt.xmlrpc.client.XmlRpcRequest;

import org.dvijok.db.DB_Object;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
/*
 * код был подкорректирован не особо вдаваясь,
 * чтобы не вызывать ошибок
 */
public class DataBaseXMLRPC {
	
	private DB_Object session;
	private XmlRpcClient client;
	
	public DataBaseXMLRPC(String xmlrpcurl) {

		client = new XmlRpcClient(xmlrpcurl);
//		client.setDebugMode(true);
		this.Restore_Session();

	}

	private void Make_Session(){
		this.Make_Session(null);
	}
	
	private void Make_Session(final DV_Request_Handler<Integer> handler){
		this.Do_Request("sessionInit", new DB_Object(), new DV_Request_Handler<DB_Object>(){
			@Override
			public void Success(DB_Object dbo) {
//				Lib.Alert("got: "+dbo);
				session = dbo.Get_DB_Object("objects").Get_DB_Object("session");
				Store_Session();
				if( handler != null ) handler.Success(0);
			}

			@Override
			public void Fail(DB_Object message) {
				if( handler != null ) handler.Fail(0);
			}
		});
	}
	
/*	protected void Check_Session(){
		this.Request("ifSessionAuth", Config_Client.getInstance().sess, rh);
	}*/
	
	protected void Store_Session(){
		Date exp_time = new Date(System.currentTimeMillis()+Resources.getInstance().conf.sess_exp_time.getTime());
		com.google.gwt.user.client.Cookies.setCookie("dvijok.session", session.Get_String("sid"), exp_time);
	}
	
	protected void Restore_Session(){
		String sid = com.google.gwt.user.client.Cookies.getCookie("dvijok.session");
		if( sid != null ){
			session = new DB_Object();
			session.put("sid", sid);
		} else Make_Session();
	}

	private String Extract(Object o){
		String s = (String)o;
		return s.toUpperCase().equals("NULL")?"":s;
	}
	
	private Serializable convert_type(Object o){
		String classname = o.getClass().getName();
//		System.out.println("  "+classname);
		
		if(classname.equals("java.util.HashMap")){
			return this.convert(o);
		} else if( classname.equals("[Ljava.lang.Object;")) {
			Object[] array = (Object[])o;
//			Serializable[] sa = new Serializable[array.length];
			DB_Object sa = new DB_Object();
			
			for(int ii=0; ii<array.length; ii++){
//				sa[ii] = this.convert_type(array[ii]);
				sa.put(Integer.toString(ii), this.convert_type(array[ii]));
			}
			return sa;
		} else if( classname.equals("java.lang.String")){
//			System.out.println("  "+(String)o);
			return (String)o;
		}
		
		return null;
	}
	
	private DB_Object convert(Object o){
		DB_Object dbo = new DB_Object();
		
    	HashMap<String,Object> obs = (HashMap<String,Object>)o;
    	
    	Iterator<String> i = obs.keySet().iterator();
    	while( i.hasNext() ){
    		String k = i.next();
//    		System.out.println(k);
    		dbo.put(k, this.convert_type(obs.get(k)));
    	}
		
		return dbo;
	}
	
	private void Do_Request(String method, DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		
		Object[] params_ = new Object[]{params};
		 
		XmlRpcRequest<Object> request = new XmlRpcRequest<Object>(
			client,
			method,
			params_,
			new AsyncCallback<Object>() {
		    public void onSuccess(Object response) {
		    	handler.Success(convert(response));
		    }

		    public void onFailure(Throwable response) {
		    	String failedMsg = response.getMessage();
//		    	Lib.Alert("DataBase: Request: request failure: "+failedMsg);
		    	handler.Fail(new DB_Object());
		    }
		});
		request.execute();
		
	}

	public void Request(final String method, final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		
		if( session != null ){
		
		    DB_Object allparams = new DB_Object();
		    allparams.put("session", session);
		    if( params != null ){
		    	allparams.put("objects", params);
		    }
		    
		    this.Do_Request(method, allparams, new DV_Request_Handler<DB_Object>(){

				@Override
				public void Success(DB_Object result) {
					if( result.Get_String("result").equals("notsid") ){
						Make_Session(new DV_Request_Handler<Integer>(){

							@Override
							public void Success(Integer result) {
								Request(method, params, handler);
							}

							@Override
							public void Fail(Integer message) {
//								handler.Fail("DataBase: Request: failed to init session: "+message);
								handler.Fail(new DB_Object());
							}
							
						});
					} else handler.Success(result);
				}

				@Override
				public void Fail(DB_Object message) {
					handler.Fail(message);
				}
		    	
		    });
	    
		} else {
			Timer t = new Timer(){
				@Override
				public void run() {
					Request(method, params, handler);
				}
			};
			
			t.schedule(500);
		}
		
	}
	
	public void Get_DB_Object(String dbid, final DV_Request_Handler<DB_Object> handler){
		DB_Object dbidobj = new DB_Object();
		dbidobj.put("dbid", dbid);
		this.Request("getObject", dbidobj, new DV_Request_Handler<DB_Object>(){

			@Override
			public void Success(DB_Object result) {
//				Lib.Alert(""+result);
				String res = result.Get_String("result");
				if( res.equals("success") ) handler.Success(result.Get_DB_Object("objects"));
				else handler.Fail(new DB_Object());
			}

			@Override
			public void Fail(DB_Object message) {
				handler.Fail(message);
			}
			
		});
	}
	
}
