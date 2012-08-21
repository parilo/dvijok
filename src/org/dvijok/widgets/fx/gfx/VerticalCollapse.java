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

package org.dvijok.widgets.fx.gfx;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class VerticalCollapse implements GFX {

	private Widget widget;
	private double height;
	private Timer t;
	private CustomEventTool endET;
	private double startHeight;
	private double endHeight;
	private int length; //in ms
	private int stepsCount;
	
	public VerticalCollapse(){
		endET = new CustomEventTool();
		endHeight = 0;
		length = 100; // in ms
		stepsCount = 10;
	}

	@Override
	public void init() {
		startHeight = widget.getOffsetHeight();
	}
	
	@Override
	public void start() {
		int delay = length/stepsCount;
		height = startHeight;
		final double step = startHeight/stepsCount;
		t = new Timer(){
			@Override
			public void run() {
				height -= step;
				if( height <= endHeight ){
					t.cancel();
					widget.getElement().getStyle().setHeight(0, Style.Unit.PX);
					endET.invokeListeners();
					return;
				}
				widget.getElement().getStyle().setHeight(height, Style.Unit.PX);
			}
		};
		t.scheduleRepeating(delay);
	}

	@Override
	public void setWidget(Widget w) {
		widget = w;
	}

	@Override
	public void addEndListener(CustomEventListener listener) {
		endET.addCustomEventListener(listener);
	}

	@Override
	public void setProperties(DBObject props) {
		if( props.containsKey("endHeight") ) endHeight = props.getDouble("endHeight");
		if( props.containsKey("length") ) length = props.getInt("length");
		if( props.containsKey("stepsCount") ) stepsCount = props.getInt("stepsCount");
	}

}
