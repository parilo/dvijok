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

package org.dvijok.controls.zurb;

import java.util.Iterator;

import org.dvijok.controls.LI;
import org.dvijok.controls.UL;
import org.dvijok.controls.select.IsSelectable;
import org.dvijok.controls.select.SelectModel;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;

public class Select extends ComplexPanel implements IsSelectable {

	private SelectModel model;
	private Label selroot;
	private UL ul;
	private InlineHTML selectedItem;
	
	private CustomEventListener selectedChanged;
	private CustomEventListener itemsChanged;
	private CustomEventTool changedET;
	
	public Select(String classes, int top){
		model = new SelectModel();
		
		selectedChanged = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				redraw();
				ul.removeStyleName("show-dropdown");
				changedET.invokeListeners(evt.getSource());
			}};
			
		itemsChanged = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				redraw();
			}};
			
		model.addSelectedChangedListener(selectedChanged);
		model.addItemsChangedListener(itemsChanged);

		changedET = new CustomEventTool();
	
		Element div = DOM.createDiv();
		div.getStyle().setDisplay(Display.INLINE);
		selroot = new Label();
		selroot.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if( !isOpened() ){
					open();
					event.stopPropagation();
				}
			}});
		
		String[] classesarr = classes.split(" ");
		for(int i=0; i<classesarr.length; i++)
			selroot.addStyleName(classesarr[i]);
		
		ul = new UL();
		ul.addStyleName("no-hover");
		ul.getElement().getStyle().setTop(top, Unit.PX);
	
		selectedItem = new InlineHTML();

		setElement(div);
		add(selroot, getElement());
		add(selectedItem, selroot.getElement());
		add(ul, selroot.getElement());
	}
	
	public boolean isOpened(){
		return ul.getElement().getClassName().contains("show-dropdown");
	}
	
	public void open(){
		ul.addStyleName("show-dropdown");
	}
	
	public void setWidth(int width){
		selroot.getElement().getStyle().setWidth(width, Unit.PX);
	}
	
	private void redraw(){
		/*
		<div href="#" class="large button dropdown">
		  Все
		  <ul>
			<li><a href="#">комната</a></li>
			<li><a href="#">пансионат</a></li>
			<li><a href="#">студия</a></li>
			<li><a href="#">1 комнатная</a></li>
			<li><a href="#">2 комнатная</a></li>
			<li><a href="#">3 комнатная</a></li>
			<li><a href="#">4 комнатная</a></li>
			<li><a href="#">5 комнатная</a></li>
			<li><a href="#">6 комнатная</a></li>
			<li><a href="#">многокомнатная</a></li>
			<li><a href="#">по суточно</a></li>
			<li><a href="#">частный дом</a></li>
			<li><a href="#">коттедж</a></li>
			<li><a href="#">дача</a></li>
			<li><a href="#">земельный участок</a></li>
			<li><a href="#">гараж</a></li>
			<li><a href="#">другое</a></li>
		  </ul>
		</div>
		*/
		
		ul.clear();

		int idx = 0;
		int selIdx = model.getSelectedIndex();
		Iterator<String> i = model.getLabels().iterator();
		while( i.hasNext() ){
			final String label = i.next();
			if( idx == selIdx ){
				selectedItem.setText(label);
			} else {
				Anchor a = new Anchor(label);
				a.addClickHandler(new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						getSelectModel().setSelectedItem(label);
						event.stopPropagation();
					}});
				LI li = new LI(a);
				ul.add(li);
			}
			idx++;
		}
		
	}

	@Override
	public SelectModel getSelectModel() {
		return model;
	}

	@Override
	public void setSelectModel(SelectModel model) {
		this.model.removeSelectedChangedListener(selectedChanged);
		this.model.removeItemsChangedListener(itemsChanged);
		this.model = model;
		model.addSelectedChangedListener(selectedChanged);
		model.addItemsChangedListener(itemsChanged);
	}
	
	@Override
	public void addChangedListener(CustomEventListener listener){
		changedET.addCustomEventListener(listener);
	}
	
	@Override
	public void removeChangedListener(CustomEventListener listener){
		changedET.removeCustomEventListener(listener);
	}
	
}
