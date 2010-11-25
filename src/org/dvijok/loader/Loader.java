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
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Sub_Panel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Loader {

	private Dwidget_Factory factory;
//	private int loadid;//used for to not mix multithreaded loading
	private HTMLPanel root;
	
	private boolean loading;
	private boolean need_load;
	
	public Loader(){
		
		this.loading = false;
		this.need_load = false;
		this.root = null;
//		this.loadid = 0;
		this.factory = new Dwidget_Factory();
		
	}
	
	public Dwidget_Factory Get_Dwidget_Factory(){
		return this.factory;
	}
	
// old method of loading widgets 		
/*	private void Do_Load_Widgets(){
		
//		//protecting from simultaneous loading from different threads
//		if(!this.loading){
		
			RootPanel.get()
		
			ArrayList<RootPanel> ws = new ArrayList<RootPanel>();
			RootPanel w;
//			this.loading = true;
			
			try{
			
			while( (w = RootPanel.get("dvijokw")) != null ){
				w.getElement().setAttribute("id", "dvijokw_");
				ws.add(w);
			}
			
			} catch(java.lang.AssertionError e){
				Lib.Alert("error");
			}
			
			for(int i=0; i<ws.size(); i++){
				w = ws.get(i);
				String name = w.getElement().getAttribute("name");
				Lib.Alert("found: "+name);
				w.add(this.factory.Get_Dwidget(name, new Sub_Panel(w)));
			}
			
//			this.loading = false;
//		}

	}*/
	
	private DB_Object Get_Param(com.google.gwt.dom.client.Element pel){
		com.google.gwt.dom.client.Element chel = pel.getNextSiblingElement();
		if( chel != null ){
			DB_Object param = new DB_Object();
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
	
/*	private int Get_Load_Id(){
		if( this.loadid > 0xFFFF ) this.loadid = 0;
		return this.loadid++; 
	}*/
	
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

//		System.out.println("loading...");
		
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
		
		
	//if will be problems with loading of sub widgets
	//maybe it is possible to rewrite loading process and Dwidget - to init widgets after loading and placing its to divs
		
/*		int id = this.Get_Load_Id();
		
		ArrayList<com.google.gwt.user.client.Element> ws = new ArrayList<com.google.gwt.user.client.Element>();
		com.google.gwt.user.client.Element w;
//		HTMLPanel html = dw.Get_HTMLPanel();

		do{
					
			for(int i=ws.size()-1; i>-1; i--){
				w = ws.get(i);
				String name = w.getAttribute("name");
//				Lib.Alert(id+" sub found: "+name);
				try{
					html.add(this.factory.Get_Dwidget(name, new Sub_Panel(w)), "dvijokw_"+id+"_"+i);
				} catch (java.util.NoSuchElementException e) {
					Lib.Alert("not found");
				}
				w.setAttribute("id", "dvijokw_");
			}
			
			ws.clear();
			int ii=0;
			
			while( (w = html.getElementById("dvijokw")) != null ){
				w.setAttribute("id", "dvijokw_"+id+"_"+ii++);
//				Lib.Alert(id+" dvijokw found : "+w.getAttribute("id"));
				ws.add(w);
			}
		
		} while( ws.size()!=0 );*/
		
	}
	
}
