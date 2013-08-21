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

package org.dvijok.widgets.checkbox;

import java.util.ArrayList;

import org.dvijok.controls.DivPanel;
import org.dvijok.controls.select.SelectModel;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Widget;

public class Toggle extends SubPanelsDwidget {
	
	private boolean checked;

	private MouseDownHandler mdhandler;
	
	private String onTmpl;
	private String offTmpl;
	
	private CustomEventTool changedET;
	
	public Toggle(){
		super("tmpl/widgets/empty.html");

		onTmpl = "tmpl/widgets/empty.html";
		offTmpl = "tmpl/widgets/empty.html";

		checked = true;
		
		changeTmpl();
		
	}
	
	public void setTemplates(
		String onTemplate,
		String offTemplate
	){
		onTmpl = onTemplate;
		offTmpl = offTemplate;
		changeTmpl();
	}
	
	private void changeTmpl(){
		if( checked ){
			changeTmpl(onTmpl);
		} else {
			changeTmpl(offTmpl);
		}
	}
	
	public boolean getValue(){
		return checked;
	}

	public void setValue(boolean value){
		if( checked != value ) setValue(value, true);
	}

	public void setValue(boolean value, boolean fireEvent){
		if( checked != value ){
			checked = value;
			changeTmpl();
			if( fireEvent ) changedET.invokeListeners(checked);
		}
	}
	
	public void toggle(){
		setValue(!checked);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		
		changedET = new CustomEventTool();
		
		mdhandler = new MouseDownHandler(){
			@Override
			public void onMouseDown(MouseDownEvent event) {
				toggle();
			}};

		addDomHandler(mdhandler, MouseDownEvent.getType());
			
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
			return null;
	}

	
	
	public void addChangedListener(CustomEventListener listener){
		changedET.addCustomEventListener(listener);
	}
	
	public void removeChangedListener(CustomEventListener listener){
		changedET.removeCustomEventListener(listener);
	}
	
}
