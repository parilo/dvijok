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
import org.dvijok.db.DB_Object;
import org.dvijok.db.DataBase;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.loader.Dwidgets;
import org.dvijok.loader.Loader;
import org.dvijok.tmpl.Tmpls_DB;
import org.dvijok.widgets.busy.Busy;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Resources {

	private static Resources self = null;
	
	public Tmpls_DB tmpls;
	public Loader loader;
	public Config conf;
	public DataBase db;
	public Dwidgets dwidgets;
	public DB_Object userInfo = null;
	private String idUserInfo;
	public String adminUrl = "http://javadimon.homelinux.net:8081/granatw/";		
	public String cabUrl = "http://javadimon.homelinux.net:8081/granatcab/";		
	
	private Busy busy;
	private int busycount;
	
	private static VerticalPanel tmpcont;
	
	public Resources(){
		this.tmpls = null;
		this.loader = null;
		this.conf = null;
		this.db = null;
		this.dwidgets = null;
		
	}

	public static Resources getInstance(){
		if(self == null){
			self = new Resources();
		}
		
		RootPanel rp = RootPanel.get("dvijokbusypane");
		rp.setStylePrimaryName("dvijoktmp");
		tmpcont = new VerticalPanel();
		rp.add(tmpcont);
		
		return self;
	}
	
	public void Get_User_Info(){
		this.Get_User_Info(null);
	}
	
	public void Get_User_Info(final DV_Request_Handler<Integer> handler){
		DB_Object reqp = new DB_Object();
		reqp.put("dbid", "userinfo");
		
		Resources.getInstance().db.Get_Object(reqp, new DV_Request_Handler<DB_Object>(){

			@Override
			public void Success(DB_Object result) {
				Resources.getInstance().userInfo = result.Get_DB_Object("objects");
				Resources.getInstance().idUserInfo = result.Get_String("id");
				if( handler != null ) handler.Success(0);
			}

			@Override
			public void Fail(DB_Object result) {
				Lib.Alert("Resources: Get_User_Info: failed: "+result);
			}
			
		});
	}
	
	public void Save_User_Info(){
		DB_Object reqp = new DB_Object();
		reqp.put("dbid", "userinfo");
		reqp.put("id", Resources.getInstance().idUserInfo);
		reqp.put("dbo", Resources.getInstance().userInfo);
		
		Resources.getInstance().db.Put_Object(reqp, new DV_Request_Handler<DB_Object>(){
			
			@Override
			public void Success(DB_Object result) {}
			
			@Override
			public void Fail(DB_Object result) {
				Lib.Alert("Resources: Save_User_Info: failed: "+result);
			}
			
		});
	}
	
	public void init(){
		busy = new Busy();
	}
	
	public void setBusy(boolean b){
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
	
}
