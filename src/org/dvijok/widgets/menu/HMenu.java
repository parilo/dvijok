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
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HMenu extends Sub_Panels_Dwidget {
	
	public HMenu(Sub_Panel p){
		super("tmpl/widgets/menu/hmenu/hmenu.html", p);
	}

	@Override
	protected void Before_Sub_Panels_Loading(){}
	
	private Widget Gen_Items(){
		HorizontalPanel hp = new HorizontalPanel();
		
		ArrayList<DBObject> params = this.Get_Params();
		for(int i=0; i<params.size(); i++){
			Menu_Item item = new Menu_Item();
			item.Set_Label(params.get(i).Get_String("LABEL"));
			item.Set_Hash(params.get(i).Get_String("VALUE"));
			hp.add(item);
		}
		
		return hp;
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("items") ) return this.Gen_Items();
		else return null;
	}
	
}
