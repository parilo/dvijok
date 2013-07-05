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

import org.dvijok.controls.select.SelectModel;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.user.client.ui.Widget;

public class Select extends SubPanelsDwidget {

	private SelectInput input;
	private SelectIcon icon;
	private SelectList list;
	
	private SelectModel model;
	
	private String selectOpenedTmpl;
	private String selectClosedTmpl;
	private boolean opened;

	private CustomEventListener selectedChanged;
	private CustomEventListener itemsChanged;
	private CustomEventTool selectedChangedET;
	
	public Select(
		String selectOpenedTemplate,
		String selectClosedTemplate,
		String selectInputTemplate,
		String selectIconOpenedTemplate,
		String selectIconClosedTemplate,
		String selectIconPushOpenedTemplate,
		String selectIconPushClosedTemplate,
		String selectListTemplate,
		String selectListItemTemplate,
		String selectListItemSelectedTemplate,
		String selectWidth
	){
		super("tmpl/widgets/empty.html");

		selectOpenedTmpl = selectOpenedTemplate;
		selectClosedTmpl = selectClosedTemplate;
		
		changeTmpl(selectClosedTmpl);
		input.setTemplate(selectInputTemplate);
		icon.setTemplate(
				selectIconOpenedTemplate,
				selectIconClosedTemplate,
				selectIconPushOpenedTemplate,
				selectIconPushOpenedTemplate
				);
		list.setTemplate(selectListTemplate);
		list.setItemTemplate(selectListItemTemplate, selectListItemSelectedTemplate);

//		selectOpenedTmpl = "tmpl/app/select/select.html";
//		selectClosedTmpl = "tmpl/app/select/select-closed.html";
//		
//		changeTmpl(selectClosedTmpl);
//		input.setTemplate("tmpl/app/select/select-input.html");
//		icon.setTemplate(
//				"tmpl/app/select/select-icon-opened.html",
//				"tmpl/app/select/select-icon-closed.html",
//				"tmpl/app/select/select-icon-push-opened.html",
//				"tmpl/app/select/select-icon-push-closed.html"
//				);
//		list.setTemplate("tmpl/app/select/select-list.html");
//		list.setItemTemplate("tmpl/app/select/select-list-item.html", "tmpl/app/select/select-list-item-selected.html");
		
		setWidth(selectWidth);
	}
	
	public void setWidth(String width){
		super.setWidth(width);
		list.setWidth(width);
	}
	
	public void setPlaceholder(String placeholder){
		input.setPlaceholder(placeholder);
	}
	
	public void setInputReadonly(boolean readonly){
		input.setInputReadonly(readonly);
	}
	
	public SelectModel getModel(){
		return model;
	}
	
	public void setModel(SelectModel model) {
//		this.model.removeSelectedChangedListener(selectedChanged);
		this.model.removeItemsChangedListener(itemsChanged);
		this.model = model;
//		model.addSelectedChangedListener(selectedChanged);
		model.addItemsChangedListener(itemsChanged);
		
		list.setSelectModel(model);
	}
	
	private void changeTmpl(){
		if( opened ){
			changeTmpl(selectOpenedTmpl);
		} else {
			changeTmpl(selectClosedTmpl);
		}
		icon.setOpened(opened);
		input.setOpened(opened);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		
		opened = false;
		
		selectedChangedET = new CustomEventTool();
		
		input = new SelectInput();
		
		icon = new SelectIcon();
		icon.addClickListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				opened = !opened;
				changeTmpl();
			}});
		
		list = new SelectList();
		list.addSelectedListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				opened = false;
				changeTmpl();
				
				input.setInputText(model.getSelectedLabel());
				selectedChangedET.invokeListeners();
			}});
		
		model = new SelectModel();
		
//		CustomEventListener selectedChanged = new CustomEventListener(){
//			@Override
//			public void customEventOccurred(CustomEvent evt) {
////				redraw();
////				ul.removeStyleName("show-dropdown");
//				changedET.invokeListeners(evt.getSource());
//			}};
			
		itemsChanged = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				list.redraw();
			}};
			
//		model.addSelectedChangedListener(selectedChanged);
		model.addItemsChangedListener(itemsChanged);

		list.setSelectModel(model);
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("input") ) return input;
		else if( dwname.equals("icon") ) return icon;
		else if( dwname.equals("list") ) return list;
		else
			return null;
	}

	
	
	public void addSelectedChangedListener(CustomEventListener listener){
		selectedChangedET.addCustomEventListener(listener);
	}
	
	public void removeSelectedChangedListener(CustomEventListener listener){
		selectedChangedET.removeCustomEventListener(listener);
	}
	
}
