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

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.widgets.Dwidget;

public class Dwidgets {

	private ArrayList<Dwidget> onAuthReinit;
	private HashMap<String, Dwidget> haveDwid;
	private HashMap<String, CustomEventTool> requestETs;
	
	public Dwidgets(){
		this.onAuthReinit = new ArrayList<Dwidget>();
		this.haveDwid = new HashMap<String, Dwidget>();
		requestETs = new HashMap<String, CustomEventTool>();
	}
	
	public void addOnAuthReload(Dwidget d){
		this.onAuthReinit.add(d);
	}
	
	public void authReload(){
		for(int i=0; i<this.onAuthReinit.size(); i++) this.onAuthReinit.get(i).reinit();
	}
	
	public void addDwidedDwidget(String dwid, Dwidget d){
		this.haveDwid.put(dwid, d);
		if( requestETs.containsKey(dwid) ){
			CustomEventTool reqET = requestETs.get(dwid);
			reqET.invokeListeners(d);
			reqET.removeAllListeners();
		}
	}
	
	public void requestDwidget(String dwid, CustomEventListener listener){
		
		if( haveDwid.containsKey(dwid) ){
			listener.customEventOccurred(new CustomEvent(haveDwid.get(dwid)));
		} else {
			CustomEventTool reqET;
			if( !requestETs.containsKey(dwid) ){
				reqET = new CustomEventTool();
				requestETs.put(dwid, reqET);
			} else reqET = requestETs.get(dwid);
			
			reqET.addCustomEventListener(listener);
		}
	}
	
	public Dwidget getDwidedDwidget(String dwid){
		return this.haveDwid.get(dwid);
	}

	@Override
	public String toString() {
		return "Dwidgets [on_auth_reinit=" + onAuthReinit + ", have_dwid="
				+ haveDwid.keySet() + "]";
	}
	
}
