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

package org.dvijok.widgets;

import org.dvijok.lib.Lib;
import org.dvijok.widgets.menu.Menu_Item;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Test extends Sub_Panels_Dwidget {

	public Test(Sub_Panel p){
		super("/tmpl/widgets/test.html", p);
	}

	@Override
	protected Widget Gen_Sub_Widget(final String dwname){
		Menu_Item mi = new Menu_Item();
		mi.Set_Label(dwname);
		mi.Set_Hash("aaa:"+dwname);
		return mi;
	}
	
}
