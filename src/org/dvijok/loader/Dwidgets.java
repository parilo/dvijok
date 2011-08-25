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
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Dwidget;

public class Dwidgets {

	private ArrayList<Dwidget> on_auth_reinit;
	private HashMap<String, Dwidget> have_dwid;
	private HashMap<String, CustomEventTool> requestETs;
	
	public Dwidgets(){
		this.on_auth_reinit = new ArrayList<Dwidget>();
		this.have_dwid = new HashMap<String, Dwidget>();
		requestETs = new HashMap<String, CustomEventTool>();
	}
	
	public void Add_On_Auth_Reload(Dwidget d){
		this.on_auth_reinit.add(d);
	}
	
	public void Auth_Reload(){
		for(int i=0; i<this.on_auth_reinit.size(); i++) this.on_auth_reinit.get(i).Reinit();
	}
	
	public void Add_Dwided_Dwidget(String dwid, Dwidget d){
		this.have_dwid.put(dwid, d);
		if( requestETs.containsKey(dwid) ){
			CustomEventTool reqET = requestETs.get(dwid);
			reqET.invokeListeners(d);
			reqET.removeAllListeners();
		}
	}
	
	public void Request_Dwidget(String dwid, CustomEventListener listener){
		
		if( have_dwid.containsKey(dwid) ){
			listener.customEventOccurred(new CustomEvent(have_dwid.get(dwid)));
		} else {
			CustomEventTool reqET;
			if( !requestETs.containsKey(dwid) ){
				reqET = new CustomEventTool();
				requestETs.put(dwid, reqET);
			} else reqET = requestETs.get(dwid);
			
			reqET.addCustomEventListener(listener);
		}
	}
	
	public Dwidget Get_Dwided_Dwidget(String dwid){
//		Lib.Alert("dwids: "+have_dwid);
		return this.have_dwid.get(dwid);
	}

	@Override
	public String toString() {
		return "Dwidgets [on_auth_reinit=" + on_auth_reinit + ", have_dwid="
				+ have_dwid.keySet() + "]";
	}
	
}
