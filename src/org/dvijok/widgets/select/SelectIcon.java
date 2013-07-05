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

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Widget;

public class SelectIcon extends SubPanelsDwidget {

	private String iconOpenedTmpl;
	private String iconClosedTmpl;
	private String iconPushOpenedTmpl;
	private String iconPushClosedTmpl;
	
	private boolean opened;
	private boolean pushed;
	
	private CustomEventTool clickET;
	
	public SelectIcon(){
		super("tmpl/widgets/empty.html");
		
		setInline(true);
	}

	public void setTemplate(
			String iconOpenedTmpl,
			String iconClosedTmpl,
			String iconPushOpenedTmpl,
			String iconPushClosedTmpl
	){
		this.iconOpenedTmpl = iconOpenedTmpl;
		this.iconClosedTmpl = iconClosedTmpl;
		this.iconPushOpenedTmpl = iconPushOpenedTmpl;
		this.iconPushClosedTmpl = iconPushClosedTmpl;
		choiseTmpl();
	}
	
	private void choiseTmpl(){
		if( opened ){
			if( pushed ){
				changeTmpl(iconPushOpenedTmpl);
			} else {
				changeTmpl(iconOpenedTmpl);
			}
		} else {
			if( pushed ){
				changeTmpl(iconPushClosedTmpl);
			} else {
				changeTmpl(iconClosedTmpl);
			}
		}
	}
	
	public boolean isOpened() {
		return opened;
	}

	public void setOpened(boolean opened) {
		this.opened = opened;
		choiseTmpl();
	}

	@Override
	protected void beforeSubPanelsLoading() {
//		<div style="border-bottom-right-radius: 0;" class="calendar-note-select-icon"><img src="images/form-select-icon.png"></div>
		
		opened = false;
		pushed = false;
		
		clickET = new CustomEventTool();
		
		addDomHandler(new MouseDownHandler(){
			@Override
			public void onMouseDown(MouseDownEvent event) {
				pushed = true;
				choiseTmpl();
			}}, MouseDownEvent.getType());
		
		addDomHandler(new MouseUpHandler(){
			@Override
			public void onMouseUp(MouseUpEvent event) {
				pushed = false;
				choiseTmpl();
				clickET.invokeListeners();
			}}, MouseUpEvent.getType());
		
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		return null;
	}
	
	
	
	public void addClickListener(CustomEventListener listener){
		clickET.addCustomEventListener(listener);
	}
	
	public void removeClickListener(CustomEventListener listener){
		clickET.removeCustomEventListener(listener);
	}

}
