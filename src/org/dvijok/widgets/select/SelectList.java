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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SelectList extends SubPanelsDwidget {

	private DivPanel list;
	
	private String itemTmpl;
	private String itemSelectedTmpl;
	
	private SelectModel model;
	
	private CustomEventListener itemClicked;
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
			
//			if( idx == selIdx ){
//				selectedItem.setText(label);
//			}
			
			list.addWidget(sli);
			
//			Anchor a = new Anchor(label);
//			a.addClickHandler(new ClickHandler(){
//				@Override
//				public void onClick(ClickEvent event) {
//					getSelectModel().setSelectedItem(label);
//					event.stopPropagation();
//				}});
//			LI li = new LI(a);
//			ul.add(li);
			
			idx++;
		}
		
	}
	
	public void setSelectModel(SelectModel model){
		this.model = model;
		redraw();
	}
	
	@Override
	protected void initInternals() {
//		<input class="calendar-note-textmini calendar-note-text" style="border-bottom-left-radius: 0;" type="text" placeholder="Месяц"/>
//		input = new TextBox();
//		input.addStyleName("calendar-note-textmini");
//		input.addStyleName("calendar-note-text");
		
		itemTmpl = "tmpl/widgets/empty.html";
		
		selectedET = new CustomEventTool();
		
		//class="calendar-note-select-list"
		list = new DivPanel();
		list.addStyleName("calendar-note-select-list");
		
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
