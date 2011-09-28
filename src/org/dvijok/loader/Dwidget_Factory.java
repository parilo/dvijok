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

import java.util.HashMap;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Dwidget_Creator;
import org.dvijok.widgets.Sub_Panel;

public class Dwidget_Factory {

	private HashMap<String,Dwidget_Creator> creators;
	private CustomEventTool dwidgetLoadET;
	
	public Dwidget_Factory(){
		this.creators = new HashMap<String,Dwidget_Creator>();
		dwidgetLoadET = new CustomEventTool();
	}
	
	public void addDwidgetLoadListener(CustomEventListener listener){
		dwidgetLoadET.addCustomEventListener(listener);
	}
	
	public void removeDwidgetLoadListener(CustomEventListener listener){
		dwidgetLoadET.removeCustomEventListener(listener);
	}
	
	public void Register(String name, Dwidget_Creator dwc ){
		this.creators.put(name, dwc);
	}
	
	public Dwidget Get_Dwidget(String name, Sub_Panel p){
		if( this.creators.containsKey(name) ){
			Dwidget_Creator dc = this.creators.get(name);
			Dwidget d = dc.Get_Dwidget(p);
			
			String dwid = d.Get_dwid();
			if( dwid != null )
				if(!dwid.equals("")) Resources.getInstance().dwidgets.Add_Dwided_Dwidget(dwid, d);
					
			if( dc.Need_Auth_Reinit() ) Resources.getInstance().dwidgets.Add_On_Auth_Reload(d);
			
			dwidgetLoadET.invokeListeners(d);
			
			return d;
		} else return new Dwidget("tmpl/components/"+name+".html");
	}
	
	
	
}
