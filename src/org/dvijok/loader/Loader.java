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
import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.SubPanel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Loader {

	private DwidgetFactory factory;
//	private HTMLPanel root;
//	private RootPanel root;
	
	private boolean loading;
	private boolean needLoad;
	
	public Loader(){
		
		this.loading = false;
		this.needLoad = false;
//		this.root = null;
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
	
	public void load(){
		Load();
	}
	
	public void loadNew(){
		Load();
	}
	
	private void Load(){

		//protecting from simultaneous loading from different threads
		if(!this.loading){

			this.loading = true;
			
			Document doc = RootPanel.get().getElement().getOwnerDocument();
			com.google.gwt.dom.client.Element w;
			while( (w = doc.getElementById("dvijokw")) != null ){
				w.setAttribute("id", "dvijokw_l");
				String name = w.getAttribute("name");
				Dwidget dw = this.factory.getDwidget(name, new SubPanel(w));
				w.getParentElement().replaceChild(dw.getElement(), w);
				dw._afterLoad();
			}
			
			this.loading = false;

			if( this.needLoad == true ){
				this.needLoad = false;
				this.loadNew();
			}
			
		} else this.needLoad = true;
		
	}
	
	/*
	 * return count Dwidget that not loaded on attaching
	 */
	public ArrayList<Dwidget> load(HTMLPanel html){

		//protecting from simultaneous loading from different threads
//		if(!this.loading){

//			this.loading = true;
			
			ArrayList<Dwidget> dwidgets = new ArrayList<Dwidget>();
		
			com.google.gwt.user.client.Element w;
			if( (w = html.getElementById("dvijokw")) != null ){
				w.setAttribute("id", "dvijokw_l");
				String name = w.getAttribute("name");
				Dwidget dw = this.factory.getDwidget(name, new SubPanel(w));
				dwidgets.add(dw);
				dw.beforeAttach();
				html.addAndReplaceElement(dw, (com.google.gwt.dom.client.Element)w);
				dw.afterAttach();
			}
			
			return dwidgets;
			
//			this.loading = false;

//			if( this.needLoad == true ){
//				this.needLoad = false;
//				this.loadNew();
//			}
			
//		}
		// else this.needLoad = true;
		
	}
	
}
