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

package org.dvijok.widgets.controls.popup;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class PopupContent extends Dwidget {
	
	private Image close;
	private Widget content;
	
	private CustomEventTool closeET;
	
	public PopupContent(){
		super("tmpl/widgets/controls/popup/popup_content.html");
	}
	
	public Widget getContent() {
		return content;
	}

	public void setContent(Widget content) {
		this.content = content;
		redraw();
	}
	
	private PopupContent getMe(){ return this; }

	@Override
	protected void initInternals() {
		
		closeET = new CustomEventTool();
		
		close = new Image("images/close.png");
		close.getElement().getStyle().setCursor(Cursor.POINTER);
		close.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				closeET.invokeListeners(getMe());
			}});
		
		content = new Label();
	}

	@Override
	protected Widget getSubWidget(String name) {
		if( name.equals("close") ) return close;
		else if( name.equals("content") ) return content;
		else return null; 
	}
	
	
	
	public void addCloseListener(CustomEventListener listener){
		closeET.addCustomEventListener(listener);
	}
	
	public void removeCloseListener(CustomEventListener listener){
		closeET.removeCustomEventListener(listener);
	}
	
}
