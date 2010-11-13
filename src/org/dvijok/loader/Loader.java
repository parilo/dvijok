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

import org.dvijok.db.DB_Object;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Sub_Panel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class Loader {

	private Dwidget_Factory factory;
	
	public Loader(){
		
		this.factory = new Dwidget_Factory();
		
	}
	
	public Dwidget_Factory Get_Dwidget_Factory(){
		return this.factory;
	}
	
	private void Do_Load_Widgets(){
		RootPanel w;
		while( (w = RootPanel.get("dvijokw")) != null ){
			String name = w.getElement().getAttribute("name");
			w.add(this.factory.Get_Dwidget(name, /*p, w*/new Sub_Panel(w)));
			w.getElement().setAttribute("id", "dvijok_");
		}
	}
	
	private DB_Object Get_Param(com.google.gwt.dom.client.Element pel){
		com.google.gwt.dom.client.Element chel = pel.getNextSiblingElement();
		if( chel != null ){
			DB_Object param = new DB_Object();
			while( chel != null ){
//				Lib.Alert(chel.getNodeName()+" "+chel.getInnerText());
				if( chel.getNodeName().equals("PARAM") ){ /*Lib.Alert("end el");*/ return param; }
				param.put(chel.getNodeName(), chel.getInnerText());
				chel = chel.getNextSiblingElement();
			}
			return param;
		}
		return null;
	}
	
	public ArrayList<DB_Object> Get_Params(Sub_Panel p){
		return this.Get_Params(p.getElement());
	}
	
	private ArrayList<DB_Object> Get_Params(Element el){

		com.google.gwt.dom.client.Element chel = el.getFirstChildElement();
		if( chel != null ){
			ArrayList<DB_Object> params = new ArrayList<DB_Object>();
			while( chel != null ){
//				Lib.Alert("loader: "+chel.getNodeName());
				if( chel.getNodeName().equals("PARAM") ) params.add(this.Get_Param(chel));
				chel = chel.getNextSiblingElement();
			}
//			Lib.Alert("params: "+params);
			return params;
		}
		
		return null;
	}
	
	public void Refresh(){
		this.Do_Load_Widgets();
	}
	
}
