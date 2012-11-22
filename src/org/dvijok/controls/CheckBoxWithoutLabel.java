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

package org.dvijok.controls;

import java.util.ArrayList;
import java.util.Iterator;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxWithoutLabel extends ComplexPanel {

	private Element check;
	private InputElement inpcheck;
	
	private boolean checked;
	private CustomEventTool valueChanged;
	
	public CheckBoxWithoutLabel(){
		checked = false;
		valueChanged = new CustomEventTool();
		
		check = DOM.createInputCheck();
		inpcheck = InputElement.as(check);
		setElement(check);
		
//		Event.addNativePreviewHandler(new NativePreviewHandler(){
//			@Override
//			public void onPreviewNativeEvent(NativePreviewEvent event) {
//				event.
//			}});
	}

	public void addValueChangeHandler(CustomEventListener listener) {
		valueChanged.addCustomEventListener(listener);
	}

	public Boolean getValue() {
		return checked;
	}

	public void setValue(Boolean value) {
		setValue(value, true);
	}

	public void setValue(Boolean value, boolean fireEvents) {
		if( checked != value ){
			checked = value;
			inpcheck.setChecked(checked);
			if( fireEvents ) valueChanged.invokeListeners(checked);
		}
	}
	
}
