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

import org.dvijok.db.DBObject;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Sub_Panel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Loader {

	private Dwidget_Factory factory;
	private HTMLPanel root;
	
	private boolean loading;
	private boolean need_load;
	
	public Loader(){
		
		this.loading = false;
		this.need_load = false;
		this.root = null;
		this.factory = new Dwidget_Factory();
		
	}
	
	public Dwidget_Factory Get_Dwidget_Factory(){
		return this.factory;
	}
	
	private DBObject Get_Param(com.google.gwt.dom.client.Element pel){
		com.google.gwt.dom.client.Element chel = pel.getNextSiblingElement();
		if( chel != null ){
			DBObject param = new DBObject();
			while( chel != null ){
//				Lib.Alert(chel.getNodeName()+" "+chel.getInnerText());
				if( chel.getNodeName().equals("PARAM") ){ /*Lib.Alert("end el");*/ return param; }
				param.put(chel.getNodeName(), chel.getInnerHTML());
				chel = chel.getNextSiblingElement();
			}
			return param;
		}
		return null;
	}
	
	public String Get_Attribute(Sub_Panel p, String name){
		return p.getElement().getAttribute(name);
	}
	
	public ArrayList<DBObject> Get_Params(Sub_Panel p){
		return this.Get_Params(p.getElement());
	}
	
	public ArrayList<DBObject> Get_Params(Element el){

		com.google.gwt.dom.client.Element chel = el.getFirstChildElement();
		if( chel != null ){
			ArrayList<DBObject> params = new ArrayList<DBObject>();
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
	
	private void Init_Root(){
		RootPanel p = RootPanel.get("dvijokroot");
		String html = p.getElement().getInnerHTML();
		p.clear();
		p.getElement().setInnerHTML("");
		this.root = new HTMLPanel(html);
		p.add(root);
	}
	
	public void Load(){
		this.Init_Root();
		this.Load(this.root);
	}
	
	public void Load_New(){
		this.Load(this.root);
	}
	
	private void Load(HTMLPanel html){

		//protecting from simultaneous loading from different threads
		if(!this.loading){

			this.loading = true;
			
			com.google.gwt.user.client.Element w;
			if( (w = html.getElementById("dvijokw")) != null ){
				w.setAttribute("id", "dvijokw_l");
				String name = w.getAttribute("name");
//				Lib.Alert("found: "+name);
				html.add(this.factory.Get_Dwidget(name, new Sub_Panel(w)), "dvijokw_l");
				w.setAttribute("id", "dvijokw_");
			}
			
			this.loading = false;

			if( this.need_load == true ){
				this.need_load = false;
				this.Load_New();
			}
			
		} else this.need_load = true;
		
	}
	
}
