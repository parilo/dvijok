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

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class FadeOut implements GFX {

	private Widget widget;
	private double opacity;
	private Timer t;
	private CustomEventTool endET;
	private double startOpacity;
	private int length; //in ms
	private int stepsCount;
	
	public FadeOut(){
		endET = new CustomEventTool();
		startOpacity = 1;
		length = 100;
		stepsCount = 20;
	}
	
	@Override
	public void start() {
		int delay = length/stepsCount;
		opacity = startOpacity;
		final double step = startOpacity/stepsCount;
		t = new Timer(){
			@Override
			public void run() {
				opacity -= step;
				if( opacity < 0 ){
					t.cancel();
					endET.invokeListeners();
					return;
				}
				widget.getElement().getStyle().setOpacity(opacity);
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
		if( props.containsKey("startOpacity") ) startOpacity = props.getDouble("startOpacity");
		if( props.containsKey("length") ) length = props.getInt("length");
		if( props.containsKey("stepsCount") ) stepsCount = props.getInt("stepsCount");
	}

	@Override
	public void init() {
	}

}
