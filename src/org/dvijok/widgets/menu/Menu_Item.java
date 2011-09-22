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

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Menu_Item extends Sub_Panels_Dwidget {

	private Label l;
	private CustomEventTool actionET;
	
	public Menu_Item(){
		super("tmpl/widgets/menu/menu_item.html");
	}
	
	public void addActionListener(CustomEventListener listener){
		actionET.addCustomEventListener(listener);
	}
	
	public void removeActionListener(CustomEventListener listener){
		actionET.removeCustomEventListener(listener);
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		actionET = new CustomEventTool();
		this.l = new Label();
	}
	
	public void Set_Label(String label){
		this.l.setText(label);
	}
	
	public void Set_Hash(final String hash){
		this.l.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Lib.Change_Hash_Token(hash);
				actionET.invokeListeners(hash);
			}
		});
	}

	@Override
	protected Widget Gen_Sub_Widget(final String dwname, ArrayList<DBObject> params){
		return this.l;
	}
	
}
