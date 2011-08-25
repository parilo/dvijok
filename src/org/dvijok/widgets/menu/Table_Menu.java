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
import org.dvijok.db.DB_Object;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.user.client.ui.Widget;

public class Table_Menu extends Sub_Panels_Dwidget {

	private Table menu;
	private int hcount;
	private CustomEventTool actionET;
	
	public Table_Menu(Sub_Panel p){
		super("tmpl/widgets/menu/table_menu/table_menu.html", p);
	}
	
	public void addActionListener(CustomEventListener listener){
		actionET.addCustomEventListener(listener);
	}
	
	public void removeActionListener(CustomEventListener listener){
		actionET.removeCustomEventListener(listener);
	}

	private void Init_Menu(){
		DB_Object req = new DB_Object();
		req.put("dbid", this.Get_dbid());
		Resources.getInstance().db.Get_Object(req, new DV_Request_Handler<DB_Object>(){

			@Override
			public void Success(DB_Object result) {
				DB_Object dbos = result.Get_DB_Object("objects");
				if( dbos.containsKey("props") ) Init_Props(dbos.Get_DB_Object("props"));
				if( dbos.containsKey("items") ) Init_Items(dbos.Get_DB_Object("items"));
			}

			@Override
			public void Fail(DB_Object result) {
				Lib.Alert("Table_Menu: Init_Menu: fail: "+result);
			}
			
		});
	}
	
	private void Init_Props(DB_Object dbo){
		if( dbo.containsKey("horiz_count") ){
			this.hcount = dbo.Get_Int("horiz_count");
			this.menu.Set_Column_Number(this.hcount);
		}
	}
	
	private void Init_Items(DB_Object dbo){
		int i=1;
		String key = "0";
		while( dbo.containsKey(key) ){
			DB_Object itdbo = dbo.Get_DB_Object(key);
			key = Integer.toString(i++);
			
			Menu_Item item = new Menu_Item();
			item.Set_Label(itdbo.Get_String("label"));
			item.Set_Hash(itdbo.Get_String("hash"));
			item.addActionListener(new CustomEventListener(){
				@Override
				public void customEventOccurred(CustomEvent evt) {
					actionET.invokeListeners(evt.getSource());
				}
			});
			this.menu.Add_Cell(item);
		}
	}
	
	@Override
	protected void Before_Sub_Panels_Loading() {
		actionET = new CustomEventTool();
		this.menu = new Table();
		this.menu.setStyleName("dwtablemenu");
		this.Init_Menu();
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		return this.menu;
	}
	
}
