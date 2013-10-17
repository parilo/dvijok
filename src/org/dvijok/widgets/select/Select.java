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
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class Select extends SubPanelsDwidget {

	private SelectInput input;
	private SelectIcon icon;
	private SelectList list;
	private FocusPanel iconFP;
	
	private SelectModel model;
	
	private String selectOpenedTmpl;
	private String selectClosedTmpl;
	private boolean opened;
	private boolean isMouseIn;

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
		
		setWidth(selectWidth);
		setListZIndex(10);
	}
	
	private Select getMe(){
		return this;
	}
	
	public void setWidth(String width){
		super.setWidth(width);
		list.setWidth(width);
	}
	
	public void setListZIndex(int zindex){
		list.setZIndex(zindex);
	}
	
	public void setPlaceholder(String placeholder){
		input.setPlaceholder(placeholder);
	}
	
	public void setText(String text){
		input.setText(text);
	}
	
	public String getText(){
		return input.getText();
	}
	
	public void setInputReadonly(boolean readonly){
		input.setInputReadonly(readonly);
	}
	
	public void setEnabled(boolean enabled){
		input.setEnabled(enabled);
		icon.setEnabled(enabled);
		setListOpened(false);
	}
	
	public void setFocus(boolean focused){
		input.setFocus(focused);
	}
	
	public SelectModel getModel(){
		return model;
	}
	
	public void setModel(SelectModel model) {
		this.model.removeSelectedChangedListener(selectedChanged);
		this.model.removeItemsChangedListener(itemsChanged);
		this.model = model;
		model.addSelectedChangedListener(selectedChanged);
		model.addItemsChangedListener(itemsChanged);
		
		list.setSelectModel(model);
		updateInput();
	}
	
	private void updateInput(){
		input.setInputText(model.getSelectedLabel());
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
	
	public void setListOpened(boolean opened){
		if( this.opened != opened ){
			this.opened = opened;
			changeTmpl();
		}
	}

	@Override
	protected void beforeSubPanelsLoading() {
		
		opened = false;
		isMouseIn = false;
		
		selectedChangedET = new CustomEventTool();
		
		input = new SelectInput();
		
		icon = new SelectIcon();
		icon.addClickListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				setListOpened(!opened);
			}});
		
		list = new SelectList();
		list.addSelectedListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				setListOpened(false);

				updateInput();
				selectedChangedET.invokeListeners();
			}});

		addDomHandler(new MouseOverHandler(){
			@Override
			public void onMouseOver(MouseOverEvent event) {
				isMouseIn = true;
			}}, MouseOverEvent.getType());

		addDomHandler(new MouseOutHandler(){
			@Override
			public void onMouseOut(MouseOutEvent event) {
				isMouseIn = false;
			}}, MouseOutEvent.getType());
		
		iconFP = new FocusPanel();
		iconFP.add(list);
		iconFP.addBlurHandler(new BlurHandler(){
			@Override
			public void onBlur(BlurEvent event) {
				if( !isMouseIn ) setListOpened(false);
			}});
		
		iconFP.addAttachHandler(new AttachEvent.Handler(){
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if( opened ) iconFP.setFocus(true);
			}});
		
		model = new SelectModel();
		
		selectedChanged = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				updateInput();
			}};
			
		itemsChanged = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				list.redraw();
			}};
			
		model.addSelectedChangedListener(selectedChanged);
		model.addItemsChangedListener(itemsChanged);

		list.setSelectModel(model);
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("input") ) return input;
		else if( dwname.equals("icon") ) return icon;
		else if( dwname.equals("list") ) return iconFP;
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
