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
import java.util.Iterator;

import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.rpc.DBObject;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.SubPanel;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
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
	private HashMap<Dwidget, DBObject> loadingDwidetsDataAttributes;
	
	public Loader(){
		this.factory = new DwidgetFactory();
		loadingDwidetsDataAttributes = new HashMap<Dwidget, DBObject>();
	}
	
	public DwidgetFactory getDwidgetFactory(){
		return this.factory;
	}
	
	/**
	 * load page dwidgets
	 */
	public void load(){
			
			Document doc = RootPanel.get().getElement().getOwnerDocument();
			
//			Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(doc, "dvdwidget").iterator();				
			Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(doc, "div").iterator();				
			while( i.hasNext() ){

				com.google.gwt.dom.client.Element w = i.next();
				if( w.hasAttribute("data-dvijok-dwidget-name") ){
					w.setAttribute("loading", "true");
//					String name = w.getAttribute("name");
					String name = w.getAttribute("data-dvijok-dwidget-name");
					Dwidget dw = this.factory.getDwidget(name, new SubPanel(w));
					dw.setDataAttributes(readDataAttributes(w));
					w.getParentElement().replaceChild(dw.getElement(), w);
					dw._afterLoadedByLoader();
				}
			}
		
	}
	
	/**
	 * loads sub dwidgets into given HTMLPanel
	 * @param HTMLPanel html
	 * @return ArrayList<Dwidget> - array of found sub dwidgets
	 */
	public ArrayList<Dwidget> loadSubDwidgets(HTMLPanel html){

			ArrayList<Dwidget> dwidgets = new ArrayList<Dwidget>();
			
//			Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(html, "dvdwidget").iterator();				
			Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(html, "div").iterator();				
			while( i.hasNext() ){
			
				com.google.gwt.dom.client.Element w = i.next();
				if( w.hasAttribute("data-dvijok-dwidget-name") ){
					w.setAttribute("loading", "true");
//					String name = w.getAttribute("name");
					String name = w.getAttribute("data-dvijok-dwidget-name");
					Dwidget dw = this.factory.getDwidget(name, new SubPanel(w));
					dw.setDataAttributes(readDataAttributes(w));
					dwidgets.add(dw);
//				dw.beforeAttach();
					html.addAndReplaceElement(dw, w);
//				dw.afterAttach();
				}
				
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
//		Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(html, "dvsubwidget").iterator();
		Iterator<com.google.gwt.dom.client.Element> i = getElementsByTagName(html, "div").iterator();
		while( i.hasNext() ){
				com.google.gwt.dom.client.Element w = i.next();
				if( w.hasAttribute("data-dvijok-subwidget-name") ){
//					String name = w.getAttribute("name");
					String name = w.getAttribute("data-dvijok-subwidget-name");
					w.setInnerHTML("");
					Widget sw = subWidgetsFactory.getSubWidget(name);
					if( sw instanceof Dwidget ) dwidgets.add((Dwidget)sw);
					if( sw == null ) sw = new Label("Loader: Don't know sub dwidget with name: ->"+name+"<-");
					String replid = "dw"+Resources.getInstance().globalseq++;
					w.setId(replid);
					html.addAndReplaceElement(sw, replid);
				}
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

	private ArrayList<com.google.gwt.dom.client.Element> getElementsByTagName(Document html, String tagName){
		NodeList<com.google.gwt.dom.client.Element> foundNL = html.getElementsByTagName(tagName);
		ArrayList<com.google.gwt.dom.client.Element> foundAL = new ArrayList<com.google.gwt.dom.client.Element>();
		int len = foundNL.getLength();
		for(int i=0; i<len; i++){
			foundAL.add(foundNL.getItem(i));
		}
		return foundAL;
	}
	
	// http://stackoverflow.com/questions/5126429/in-gwt-how-can-i-get-all-attributes-of-an-element-in-the-html-dom
	private DBObject readDataAttributes(com.google.gwt.dom.client.Element el){
		
		DBObject dataAttributes = new DBObject();
		
		JsArray<Node> attributes = getAttributes(el);
		for (int i = 0; i < attributes.length(); i ++) {
		    Node node = attributes.get(i);
		    String attributeName = node.getNodeName();

		    if( attributeName.length() > 4 )
		    if( attributeName.substring(0, 5).equals("data-") ){
		    	dataAttributes.put(attributeName, node.getNodeValue());
		    }
		    
		}
		
		return dataAttributes;
	}
	
	public static native JsArray<Node> getAttributes(com.google.gwt.dom.client.Element elem) /*-{
	   return elem.attributes;
	}-*/;
	
}
