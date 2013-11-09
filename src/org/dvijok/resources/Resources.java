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

package org.dvijok.resources;

import org.dvijok.config.Config;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.loader.Loader;
import org.dvijok.resources.historywatch.HistoryWatcher;
import org.dvijok.rpc.DBObject;
import org.dvijok.rpc.RPC;
import org.dvijok.tmpl.TmplsDB;
import org.dvijok.widgets.busy.BigBusy;
import org.dvijok.widgets.busy.Busy;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Resources {

	private static Resources self = null;
	
	public TmplsDB tmpls;
	public Loader loader;
	public Config conf;
	public RPC db;
//	public Dwidgets dwidgets;
	public HistoryWatcher historyWatcher;
	public DBObject userInfo = null;
	public DBObject userData = null;
	public AuthTool authTool;
	public String initTitle;
	public DBObject commonData;
	
	private Busy busy;
	private int busycount;
	
	public int globalseq;
	
	private static VerticalPanel tmpcont;
	
	public Resources(){
		
		globalseq = 0;
		
		this.tmpls = null;
		this.loader = null;
		this.conf = null;
		this.db = null;
//		this.dwidgets = null;
		authTool = new AuthTool();
		
		userInfo = new DBObject();
//		userInfo.put("fullname", "guest");
//		userInfo.put("isadmin", "0");
		
		commonData = new DBObject();
	}

	public static Resources getInstance(){
		if(self == null){
			self = new Resources();
		
			RootPanel rp = RootPanel.get("dvijokbusypane");
			rp.setStylePrimaryName("dvijoktmp");
			tmpcont = new VerticalPanel();
			rp.add(tmpcont);
			
		}
		
		return self;
	}
	
	public boolean isAuthorized(){
		if( userInfo != null ){
			return userInfo.containsKey("type") || userInfo.containsKey("role");
		}
		return false;
	}
//	
//	public void getUserInfo(String login, final DVRequestHandler<Integer> handler){
//		DBObject reqp = new DBObject();
//		reqp.put("tags", "userinfo "+login);
//		
//		Resources.getInstance().db.getObject(reqp, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				Resources.getInstance().userInfo = result;
//				if( handler != null ) handler.success(0);
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				Lib.alert("Resources: getUserInfo: failed: "+result);
//			}
//			
//		});
//	}
	
	public void saveUserData(){
		db.saveUserData(userData, new RequestHandler<DBObject>(){
			
			@Override
			public void success(DBObject result) {}
			
			@Override
			public void fail(DBObject result) {
			}
			
		});
			
//		DBObject reqp = new DBObject();
//		reqp.put("dbid", "userinfo");
//		reqp.put("id", Resources.getInstance().idUserInfo);
//		reqp.put("dbo", Resources.getInstance().userInfo);
//		
//		Resources.getInstance().db.putObject(reqp, new DVRequestHandler<DBObject>(){
//			
//			@Override
//			public void success(DBObject result) {}
//			
//			@Override
//			public void fail(DBObject result) {
//				Lib.alert("Resources: Save_User_Info: failed: "+result);
//			}
//			
//		});
	}
	
//	public void init(){
//		busy = new BigBusy();
//	}
	
	public void setBusy(boolean b){
		
		if( busy == null ) busy = new BigBusy();
		
		busycount += b?1:busycount==0?0:-1;
		
		if( b == true && busycount == 1 ){
			RootPanel.get("dvijokbusypane").add(busy);
		} else if( b == false && busycount == 0 ) {
			RootPanel.get("dvijokbusypane").remove(busy);
		}
	}
	
	public void addToTmp(Widget w){
		tmpcont.add(w);
	}
	
	public void removeFromTmp(Widget w){
		tmpcont.remove(w);
	}

	public void onAuth(DBObject userinfo){
		this.userInfo = userinfo;
//		Resources.getInstance().dwidgets.authReload();
	}
	
//	public void onAuth(String loginStr){
//		Resources.getInstance().getUserInfo(loginStr, new DVRequestHandler<Integer>(){
//
//			@Override
//			public void success(Integer result) {
//				Resources.getInstance().dwidgets.authReload();
//			}
//
//			@Override
//			public void fail(Integer result) {}
//		});
//	}
	
}
