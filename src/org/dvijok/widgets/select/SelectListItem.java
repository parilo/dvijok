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
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SelectListItem extends SubPanelsDwidget {

	private InlineHTML title;
	private int index;
	private boolean selected;
	
	private String tmpl;
	private String selectedTmpl;
	
	private CustomEventTool clickET;
	
	public SelectListItem(){
		super("tmpl/widgets/empty.html");
	}

	
	
	public void setTemplate(String tmpl, String selectedTmpl){
		this.tmpl = tmpl;
		this.selectedTmpl = selectedTmpl;
		choiseTmpl();
	}

	private void choiseTmpl(){
		if( selected ) changeTmpl(tmpl);
		else changeTmpl(selectedTmpl);
	}
	
	
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void setIndex(int idx){
		index = idx;
	}
	
	public int getIndex(){
		return index;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		choiseTmpl();
	}

	@Override
	protected void beforeSubPanelsLoading() {
		
		selected = false;
		
		title = new InlineHTML();
		
		clickET = new CustomEventTool();
		
		addDomHandler(new MouseUpHandler(){
			@Override
			public void onMouseUp(MouseUpEvent event) {
				clickET.invokeListeners(index);
			}}, MouseUpEvent.getType());
		
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("title") ) return title;
		return null;
	}
	
	
	
	public void addClickListener(CustomEventListener listener){
		clickET.addCustomEventListener(listener);
	}
	
	public void removeClickListener(CustomEventListener listener){
		clickET.removeCustomEventListener(listener);
	}
	

}
