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

package org.dvijok.db.gwtrpc;

import java.util.Date;

import org.dvijok.db.DB_Object;
import org.dvijok.db.DataBase;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DataBase_GWTRPC implements DataBase {

	private DB_Object session;
	
	private static DataBase_ServiceAsync dataSvc = GWT.create(DataBase_Service.class);
	
	public DataBase_GWTRPC(){
		this.Restore_Session();
	}

	private void Make_Session(){
		this.Make_Session(null);
	}
	
	private void Make_Session(final DV_Request_Handler<Integer> handler){
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: make session failed");
				if( handler != null ) handler.Fail(0);
			}

			@Override
			public void onSuccess(DB_Object result) {
//				Lib.Alert("sid: "+result.Get_String("sid"));
				session = result;
				Store_Session();
				if( handler != null ) handler.Success(0);
			}
			
		};
		
		dataSvc.Get_Session(cb);
	}
	
	protected void Store_Session(){
		Date exp_time = new Date(System.currentTimeMillis()+Resources.getInstance().conf.sess_exp_time.getTime());
		com.google.gwt.user.client.Cookies.setCookie("dvijok.session", session.Get_String("sid"), exp_time, Lib.getDomain(), "/", false);
	}
	
	protected void Restore_Session(){
		String sid = com.google.gwt.user.client.Cookies.getCookie("dvijok.session");
		if( sid != null ){
			session = new DB_Object();
			session.put("sid", sid);
		} else Make_Session();
	}
	
	@Override
	public void Auth(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: auth request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") ) handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Auth(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Get_CallBack: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Auth(allparams, cb);
	}

	@Override
	public void Send_Key(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: authkey request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") )	handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Send_Key(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Get_CallBack: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Send_Key(allparams, cb);
	}

	@Override
	public void Logout(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: Logout: logout request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") || res.equals("notsid") ) handler.Success(result);
				else handler.Fail(result);
			}
			
		};
		
		dataSvc.Logout(allparams, cb);
	}

	@Override
	public void Get_Object(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: authkey request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") )	handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Get_Object(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Get_Object: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Get_Object(allparams, cb);
	}

	@Override
	public void Get_Objects(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: authkey request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") )	handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Get_Objects(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Put_Object: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Get_Objects(allparams, cb);
	}

	@Override
	public void Put_Object(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: authkey request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") )	handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Put_Object(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Put_Object: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Put_Object(allparams, cb);
	}

	@Override
	public void Del_Object(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: authkey request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") )	handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Del_Object(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Put_Object: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Del_Object(allparams, cb);
	}

/*	@Override
	public void Send_Request(final DB_Object params, final DV_Request_Handler<DB_Object> handler) {
		final DB_Object allparams = new DB_Object();
		allparams.put("session", this.session);
		allparams.put("objects", params);
		
		AsyncCallback<DB_Object> cb = new AsyncCallback<DB_Object>(){

			@Override
			public void onFailure(Throwable caught) {
				Lib.Alert("DataBase_GWTRPC: authkey request failed");
			}

			@Override
			public void onSuccess(DB_Object result) {
				String res = result.Get_String("result");
				if( res.equals("success") )	handler.Success(result);
				else if( res.equals("notsid") ){
					
					Make_Session(new DV_Request_Handler<Integer>(){

						@Override
						public void Success(Integer result) {
							Send_Request(params, handler);
						}

						@Override
						public void Fail(Integer result) {
							Lib.Alert("DataBase_GWTRPC: Get_CallBack: make session failed");
						}
						
					});
					
				} else handler.Fail(result);
			}
			
		};
		
		dataSvc.Send_Request(allparams, cb);
	}*/

}
