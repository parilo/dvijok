//    dvijok - cms written in gwt
//    Copyright (C) 2010-2013  Pechenko Anton Vladimirovich aka Parilo
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

package org.dvijok.widgets.select;

import java.util.ArrayList;
import java.util.Iterator;

import org.dvijok.controls.DivPanel;
import org.dvijok.controls.LI;
import org.dvijok.controls.select.SelectModel;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SelectList extends SubPanelsDwidget {

	private DivPanel list;
	
	private String itemTmpl;
	private String itemSelectedTmpl;
	
	private SelectModel model;
	
	private CustomEventListener itemClicked;
	private CustomEventListener listClicked;
	private CustomEventTool selectedET;
	
	public SelectList(){
		super("tmpl/widgets/empty.html");
	}

	public void setTemplate(String tmpl){
		changeTmpl(tmpl);
	}
	
	public void setItemTemplate(String tmpl, String selectedTmpl){
		itemTmpl = tmpl;
		itemSelectedTmpl = tmpl;
		redraw();
	}
	
	public void setWidth(String width){
		list.setWidth(width);
	}
	
	public void setZIndex(int zindex){
		list.getElement().getStyle().setZIndex(zindex);
	}
	
	public void redraw(){
		
		list.clear();
		
		int idx = 0;
		int selIdx = model.getSelectedIndex();
		Iterator<String> i = model.getLabels().iterator();
		while( i.hasNext() ){
			
			final String label = i.next();
			SelectListItem sli = new SelectListItem();
			sli.setTemplate(itemTmpl, itemSelectedTmpl);
			sli.setTitle(label);
			sli.setIndex(idx);
			sli.addClickListener(itemClicked);
			
			list.addWidget(sli);
			
			idx++;
		}
		
	}
	
	public void setSelectModel(SelectModel model){
		this.model = model;
		redraw();
	}
	
	public void setListClicked(CustomEventListener listClicked) {
		this.listClicked = listClicked;
	}

	@Override
	protected void beforeSubPanelsLoading() {
		
		itemTmpl = "tmpl/widgets/empty.html";
		
		selectedET = new CustomEventTool();
		
		//class="calendar-note-select-list"
		list = new DivPanel();
		list.addStyleName("calendar-note-select-list");
		list.addDomHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
System.out.println("click");	
				listClicked.customEventOccurred(null);
			}}, ClickEvent.getType());

//		list.addDomHandler(new BlurHandler(){
//			@Override
//			public void onBlur(BlurEvent event) {
//System.out.println("blur");				
//			}}, BlurEvent.getType());
//
//		list.addDomHandler(new KeyDownHandler(){
//			@Override
//			public void onKeyDown(KeyDownEvent event) {
//System.out.println("key");				
//			}}, KeyDownEvent.getType());
		
		itemClicked = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				int index = (Integer)evt.getSource();
				model.setSelectedIndex(index);
				selectedET.invokeListeners();
			}};
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("list") ) return list;
		return null;
	}
	
	
	
	public void addSelectedListener(CustomEventListener listener){
		selectedET.addCustomEventListener(listener);
	}
	
	public void removeSelectedListener(CustomEventListener listener){
		selectedET.removeCustomEventListener(listener);
	}


}
