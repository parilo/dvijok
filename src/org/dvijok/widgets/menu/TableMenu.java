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

package org.dvijok.widgets.menu;

import java.util.ArrayList;

import org.dvijok.controls.Table;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.user.client.ui.Widget;

public class TableMenu extends SubPanelsDwidget {

	private Table menu;
	private int hcount;
	private CustomEventTool actionET;
	
	public TableMenu(SubPanel p){
		super("tmpl/widgets/menu/table_menu/table_menu.html", p);
	}
	
	public void addActionListener(CustomEventListener listener){
		actionET.addCustomEventListener(listener);
	}
	
	public void removeActionListener(CustomEventListener listener){
		actionET.removeCustomEventListener(listener);
	}

	private void initMenu(){
//		DBObject req = new DBObject();
//		req.put("dbid", this.getDbid());
//		Resources.getInstance().db.getObject(req, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				DBObject dbos = result.getDBObject("objects");
//				if( dbos.containsKey("props") ) initProps(dbos.getDBObject("props"));
//				if( dbos.containsKey("items") ) initItems(dbos.getDBObject("items"));
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				Lib.alert("Table_Menu: Init_Menu: fail: "+result);
//			}
//			
//		});
	}
	
	private void initProps(DBObject dbo){
		if( dbo.containsKey("horiz_count") ){
			this.hcount = dbo.getInt("horiz_count");
			this.menu.Set_Column_Number(this.hcount);
		}
	}
	
	private void initItems(DBObject dbo){
		int i=1;
		String key = "0";
		while( dbo.containsKey(key) ){
			DBObject itdbo = dbo.getDBObject(key);
			key = Integer.toString(i++);
			
			MenuItem item = new MenuItem();
			item.setLabel(itdbo.getString("label"));
			item.setHash(itdbo.getString("hash"));
			item.addActionListener(new CustomEventListener(){
				@Override
				public void customEventOccurred(CustomEvent evt) {
					actionET.invokeListeners(evt.getSource());
				}
			});
			this.menu.addCell(item);
		}
	}
	
	@Override
	protected void beforeSubPanelsLoading() {
		actionET = new CustomEventTool();
		this.menu = new Table();
		this.menu.setStyleName("dwtablemenu");
		this.initMenu();
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		return this.menu;
	}
	
}
