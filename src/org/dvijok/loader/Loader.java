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
import org.dvijok.widgets.SubPanel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class Loader {

	private DwidgetFactory factory;
	private HTMLPanel root;
	
	private boolean loading;
	private boolean needLoad;
	
	public Loader(){
		
		this.loading = false;
		this.needLoad = false;
		this.root = null;
		this.factory = new DwidgetFactory();
		
	}
	
	public DwidgetFactory getDwidgetFactory(){
		return this.factory;
	}
	
	private DBObject getParam(com.google.gwt.dom.client.Element pel){
		com.google.gwt.dom.client.Element chel = pel.getNextSiblingElement();
		if( chel != null ){
			DBObject param = new DBObject();
			while( chel != null ){
				if( chel.getNodeName().equals("PARAM") ){ return param; }
				param.put(chel.getNodeName(), chel.getInnerHTML());
				chel = chel.getNextSiblingElement();
			}
			return param;
		}
		return null;
	}
	
	public String getAttribute(SubPanel p, String name){
		return p.getElement().getAttribute(name);
	}
	
	public ArrayList<DBObject> getParams(SubPanel p){
		return this.getParams(p.getElement());
	}
	
	public ArrayList<DBObject> getParams(Element el){

		com.google.gwt.dom.client.Element chel = el.getFirstChildElement();
		if( chel != null ){
			ArrayList<DBObject> params = new ArrayList<DBObject>();
			while( chel != null ){
				if( chel.getNodeName().equals("PARAM") ) params.add(this.getParam(chel));
				chel = chel.getNextSiblingElement();
			}
			return params;
		}
		
		return null;
	}
	
	private void initRoot(){
		RootPanel p = RootPanel.get("dvijokroot");
		String html = p.getElement().getInnerHTML();
		p.clear();
		p.getElement().setInnerHTML("");
		this.root = new HTMLPanel(html);
		p.add(root);
	}
	
	public void load(){
		this.initRoot();
		this.Load(this.root);
	}
	
	public void loadNew(){
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
				html.add(this.factory.getDwidget(name, new SubPanel(w)), "dvijokw_l");
				w.setAttribute("id", "dvijokw_");
			}
			
			this.loading = false;

			if( this.needLoad == true ){
				this.needLoad = false;
				this.loadNew();
			}
			
		} else this.needLoad = true;
		
	}
	
}
