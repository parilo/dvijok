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

import org.dvijok.controls.select.SelectModel;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;

public class CheckBox extends SubPanelsDwidget {

	private Toggle toggle;
	private InlineHTML label;
	
	private String checkboxTmpl;
	
	public CheckBox(
		String title,
		String checkboxTemplate,
		String toggleOnTemplate,
		String toggleOffTemplate
	){
		super("tmpl/widgets/empty.html");

		checkboxTmpl = checkboxTemplate;
		
		changeTmpl(checkboxTmpl);
		
		toggle.setTemplates(toggleOnTemplate, toggleOffTemplate);
		label.setText(title);
	}
	
	public boolean getValue(){
		return toggle.getValue();
	}
	
	public void setValue(boolean value){
		toggle.setValue(value);
	}
	
	public void setValue(boolean value, boolean fireEvent){
		toggle.setValue(value, fireEvent);
	}
	
	public void setTitle(String title){
		label.setText(title);
	}

	@Override
	protected void initInternals() {

		toggle = new Toggle();
		
		label = new InlineHTML();
		label.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				toggle.toggle();
			}});
		
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("toggle") ) return toggle;
		else if( dwname.equals("label") ) return label;
		else
			return null;
	}

	
	
	public void addChangedListener(CustomEventListener listener){
		toggle.addChangedListener(listener);
	}
	
	public void removeChangedListener(CustomEventListener listener){
		toggle.addChangedListener(listener);
	}
	
}
