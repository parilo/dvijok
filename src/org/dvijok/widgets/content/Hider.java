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

package org.dvijok.widgets.content;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Hider extends SubPanelsDwidget {

	private Anchor hidew;
	private SimplePanel widget;
	private Widget modeFirstWidget;
	private Widget modeSecondWidget;
	private boolean mode; //true == first, false == second
	
	public Hider(SubPanel p){
		super("tmpl/widgets/content/hider.html", p);
	}
	
	public Hider(){
		super("tmpl/widgets/content/hider.html");
	}

	public Widget getModeFirstWidget() {
		return modeFirstWidget;
	}

	public void setModeFirstWidget(Widget modeFirstWidget) {
		this.modeFirstWidget = modeFirstWidget;
		if( mode ) widget.setWidget(modeFirstWidget);
	}

	public Widget getModeSecondWidget() {
		return modeSecondWidget;
	}

	public void setModeSecondWidget(Widget modeSecondWidget) {
		this.modeSecondWidget = modeSecondWidget;
		if( !mode ) widget.setWidget(modeSecondWidget);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		hidew = new Anchor("");
//		hidew.setHTML("&emsp;&#9660;&emsp;");
		hidew.setHTML("&#9660");
		hidew.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if( mode ){
//					Lib.alert(""+modeSecondWidget);
					widget.setWidget(modeSecondWidget);
					hidew.setHTML("&#9650");
				} else {
//					Lib.alert(""+modeFirstWidget);
					widget.setWidget(modeFirstWidget);
					hidew.setHTML("&#9660");
				}
				mode = !mode;
			}});
		
		widget = new SimplePanel();
		modeFirstWidget = new Label("");
		modeSecondWidget = new Label("");
		mode = true;
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("hide") ) return hidew;
		else if( dwname.equals("widget") ) return widget;
		else return null;
	}
	
}
