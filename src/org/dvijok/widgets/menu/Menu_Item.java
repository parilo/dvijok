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

import org.dvijok.lib.Lib;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Menu_Item extends Sub_Panels_Dwidget {

	private Label l;
	
	public Menu_Item(){
		super("/tmpl/widgets/menu/menu_item.html");
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
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
			}
		});
	}

	@Override
	protected Widget Gen_Sub_Widget(final String dwname){
		return this.l;
	}
	
}
