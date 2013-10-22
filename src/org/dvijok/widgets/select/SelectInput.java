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

import org.dvijok.controls.DivPanel;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class SelectInput extends SubPanelsDwidget {

	private DivPanel w;
	private TextBox input;

	private boolean opened;
	
	private CustomEventListener blurL;
	private CustomEventListener blurByTabL;
	
	public SelectInput(){
		super("tmpl/widgets/empty.html");
		
		setInline(true);
	}

	public void setTemplate(String tmpl){
		changeTmpl(tmpl);
	}
	
	public void setWidth(String width){
		w.setWidth(width);
	}
	
	public void setPlaceholder(String placeholder){
		input.getElement().setAttribute("placeholder", placeholder);
	}
	
	public void setText(String text){
		input.setText(text);
	}
	
	public String getText(){
		return input.getText();
	}
	
	public void setInputReadonly(boolean readonly){
		input.setReadOnly(readonly);
	}
	
	public void setEnabled(boolean enabled){
		input.setEnabled(enabled);
	}
	
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
		if( opened ) input.addStyleName("calendar-note-select-input-with-list");
		else input.removeStyleName("calendar-note-select-input-with-list");
	}
	
	public void setInputText(String text){
		input.setText(text);
	}
	
	public void setFocus(boolean focused){
		input.setFocus(focused);
	}
	
//	public HandlerRegistration addBlurHandler(BlurHandler handler){
//		return input.addBlurHandler(handler);
//	}
	
	public void setBlurListener(CustomEventListener listener){
		blurL = listener;
	}
	
	public void setBlurByTabListener(CustomEventListener listener){
		blurByTabL = listener;
	}

	@Override
	protected void beforeSubPanelsLoading() {
		input = new TextBox();
		input.addStyleName("calendar-note-textmini");
		input.addStyleName("calendar-note-text");
		input.setWidth("100%");

		input.addBlurHandler(new BlurHandler(){
			@Override
			public void onBlur(BlurEvent event) {
				blurL.customEventOccurred(null);
			}});
		
		input.addKeyDownHandler(new KeyDownHandler(){
			@Override
			public void onKeyDown(KeyDownEvent event) {
		      switch (event.getNativeKeyCode()) {
//		        case KeyCodes.KEY_ENTER:
		        case KeyCodes.KEY_TAB:
		        	blurByTabL.customEventOccurred(null);
		        	break;
//		        case KeyCodes.KEY_ESCAPE:
//		        case KeyCodes.KEY_UP:
//		          break;
//		        case KeyCodes.KEY_DOWN:
//		          break;
		      }
			}});
		
		w = new DivPanel();
		w.addWidget(input);
		w.getElement().getStyle().setFloat(Float.LEFT);
		w.setWidth("100%");
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("input") ) return w;
		else
			return null;
	}

}
