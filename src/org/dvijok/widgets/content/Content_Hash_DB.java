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

package org.dvijok.widgets.content;

import java.util.ArrayList;
import java.util.HashMap;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Content_Hash_DB extends Sub_Panels_Dwidget {

	private SimplePanel content;
	private HashMap<String,HTMLPanel> contents;
	private HashMap<String,Boolean> loaded;
//	private CustomEventTool changeET;
	
	public Content_Hash_DB(Sub_Panel p){
		super("tmpl/widgets/content/content_hash/content_hash.html", p);
		
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String hash = event.getValue();
//				changeET.invokeListeners(hash);
				Load_Content(hash);
			}
		});
		
	}
	
//	public void addPageHashChangeListener(CustomEventListener listener){
//		changeET.addCustomEventListener(listener);
//	}
//	
//	public void removePageHashChangeListener(CustomEventListener listener){
//		changeET.removeCustomEventListener(listener);
//	}

	@Override
	public void Reinit() {
		this.contents.clear();
		this.loaded.clear();
		this.Init_Contents();
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
//		changeET = new CustomEventTool();
		this.content = new SimplePanel();
		this.contents = new HashMap<String,HTMLPanel>();
		this.loaded = new HashMap<String,Boolean>();
		
		this.Init_Contents();
	}

	private void Load_Content(String hash){
		if( contents.containsKey(hash) ){
			this.content.setWidget(contents.get(hash));
			this.Load_Dwidgets(hash);
		} else if( contents.containsKey("def") ){
			this.content.setWidget(contents.get("def"));
			this.Load_Dwidgets("def");
		} else {
			this.content.clear();
		}
	}
	
	private void Load_Dwidgets(String hash){
		if(!this.loaded.get(hash)){
			Resources.getInstance().loader.Load_New();
			this.loaded.put(hash, true);
		}
	}
	
	private void Init_Contents(){
		DBObject req = new DBObject();
		req.put("dbid", this.Get_dbid());
		Resources.getInstance().db.Get_Object(req, new DV_Request_Handler<DBObject>(){

			@Override
			public void Success(DBObject result) {
				Init_Contents(result.Get_DB_Object("objects"));
				Load_Content(Lib.Get_Hash_Token());
			}

			@Override
			public void Fail(DBObject result) {
				Lib.Alert("Content_Hash_DB: Init_Contents: fail: "+result);
			}
			
		});
	}
	
	private void Init_Contents(DBObject dbo){
		for(String hash : dbo.keySet()){
			String cont = dbo.Get_String(hash);
			if( hash.equals("#def") ) hash = "def";
			this.contents.put(hash , new HTMLPanel(cont));
			this.loaded.put(hash, false);
		}
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return this.content;
		} else return null;
	}
	
}
