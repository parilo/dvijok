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
import java.util.Iterator;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.SubPanel;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class Loader {

	private DwidgetFactory factory;
	
	public Loader(){
		this.factory = new DwidgetFactory();
	}
	
	public DwidgetFactory getDwidgetFactory(){
		return this.factory;
	}
	
	/**
	 * load page dwidgets
	 */
	public void load(){
			
			Document doc = RootPanel.get().getElement().getOwnerDocument();
			
			NodeList<com.google.gwt.dom.client.Element> dvdwidgetEls = doc.getElementsByTagName("dvdwidget");
			int len = dvdwidgetEls.getLength();
			for(int i=0; i<len; i++){
				com.google.gwt.dom.client.Element w = dvdwidgetEls.getItem(i);
				w.setAttribute("loading", "true");
				String name = w.getAttribute("name");
				Dwidget dw = this.factory.getDwidget(name, new SubPanel(w));
				w.getParentElement().replaceChild(dw.getElement(), w);
				dw._afterLoadedByLoader();
			}
		
	}
	
	/**
	 * loads sub dwidgets into given HTMLPanel
	 * @param HTMLPanel html
	 * @return ArrayList<Dwidget> - array of found sub dwidgets
	 */
	public ArrayList<Dwidget> loadSubDwidgets(HTMLPanel html){

			ArrayList<Dwidget> dwidgets = new ArrayList<Dwidget>();
			
			Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(html, "dvdwidget").iterator();				
			while( i.hasNext() ){
			
				com.google.gwt.dom.client.Element w = i.next();
				w.setAttribute("loading", "true");
				String name = w.getAttribute("name");
				Dwidget dw = this.factory.getDwidget(name, new SubPanel(w));
				dwidgets.add(dw);
//				dw.beforeAttach();
				html.addAndReplaceElement(dw, w);
//				dw.afterAttach();
				
			}
			
			return dwidgets;
		
	}
	
	/**
	 * loads sub widgets into given HTMLPanel
	 * @param HTMLPanel html
	 * @return ArrayList<Dwidget> - array of found sub dwidgets
	 */
	public ArrayList<Dwidget> loadSubWidgets(HTMLPanel html, SubWidgetsFactory subWidgetsFactory) {
		
		ArrayList<Dwidget> dwidgets = new ArrayList<Dwidget>();
		
		// NodeList is live. So we need store found elements in ArrayList and process them after
		Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(html, "dvsubwidget").iterator();
		while( i.hasNext() ){
				com.google.gwt.dom.client.Element w = i.next();
				String name = w.getAttribute("name");
				w.setInnerHTML("");
				Widget sw = subWidgetsFactory.getSubWidget(name);
				if( sw instanceof Dwidget ) dwidgets.add((Dwidget)sw);
				if( sw == null ) sw = new Label("Loader: Don't know sub dwidget with name: ->"+name+"<-");
				String replid = "dw"+Resources.getInstance().globalseq++;
				w.setId(replid);
				html.addAndReplaceElement(sw, replid);
		}

		return dwidgets;
	}
	
	// NodeList is live. So we need store found elements in ArrayList and process them after
	private ArrayList<com.google.gwt.dom.client.Element> getElementsByTagName(HTMLPanel html, String tagName){
		NodeList<com.google.gwt.dom.client.Element> foundNL = html.getElement().getElementsByTagName(tagName);
		ArrayList<com.google.gwt.dom.client.Element> foundAL = new ArrayList<com.google.gwt.dom.client.Element>();
		int len = foundNL.getLength();
		for(int i=0; i<len; i++){
			foundAL.add(foundNL.getItem(i));
		}
		return foundAL;
	}
	
}
