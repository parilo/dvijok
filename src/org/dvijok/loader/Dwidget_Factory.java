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

package org.dvijok.loader;

import java.util.ArrayList;
import java.util.HashMap;

import org.dvijok.db.DB_Object;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Dwidget_Creator;
import org.dvijok.widgets.Sub_Panel;

import com.google.gwt.user.client.ui.RootPanel;

public class Dwidget_Factory {

	private HashMap<String,Dwidget_Creator> creators;
	
	public Dwidget_Factory(){
		this.creators = new HashMap<String,Dwidget_Creator>(); 
	}
	
	public void Register(String name, Dwidget_Creator dwc ){
		this.creators.put(name, dwc);
	}
	
	public Dwidget Get_Dwidget(String name, Sub_Panel p){
		if( this.creators.containsKey(name) ) return this.creators.get(name).Get_Dwidget(p);
		else return new Dwidget("/tmpl/components/"+name+".html");
	}
	
	
	
}
