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
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.DwidgetCreator;
import org.dvijok.widgets.SubPanel;

public class DwidgetFactory {

	private HashMap<String,DwidgetCreator> creators;
	private CustomEventTool dwidgetLoadET;
	
	public DwidgetFactory(){
		this.creators = new HashMap<String,DwidgetCreator>();
		dwidgetLoadET = new CustomEventTool();
	}
	
	public void addDwidgetLoadListener(CustomEventListener listener){
		dwidgetLoadET.addCustomEventListener(listener);
	}
	
	public void removeDwidgetLoadListener(CustomEventListener listener){
		dwidgetLoadET.removeCustomEventListener(listener);
	}
	
	public void register(String name, DwidgetCreator dwc ){
		this.creators.put(name, dwc);
	}
	
	public Dwidget getDwidget(String name, SubPanel p){
		if( this.creators.containsKey(name) ){
			DwidgetCreator dc = this.creators.get(name);
			Dwidget d = dc.getDwidget();
			
			p.clear();
			p.getElement().setInnerHTML("");
			
//			String dwid = d.getDwid();
//			if( dwid != null )
//				if(!dwid.equals("")) Resources.getInstance().dwidgets.addDwidedDwidget(dwid, d);
					
//			if( d.needAuthReinit() ) Resources.getInstance().dwidgets.addOnAuthReload(d);
			
			dwidgetLoadET.invokeListeners(d);
			
			return d;
		} else return null;
	}
	
	
	
}
