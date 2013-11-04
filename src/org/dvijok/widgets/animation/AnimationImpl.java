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

package org.dvijok.widgets.animation;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public abstract class AnimationImpl implements Animation {

	private int timeStep;//in millis
	
	private Timer timer;
	private Widget widget;
	
	private CustomEventTool tickET;
	private CustomEventTool endET;
	
	public AnimationImpl(){
		tickET = new CustomEventTool();
		endET = new CustomEventTool();
		
		timer = new Timer(){
			@Override
			public void run() {
				tick();
			}};
	}
	
	/**
	 * widget is already attached - you can define
	 * height and other parameters in this function 
	 */
	public void init(){
		timeStep = 25;
		reset();
	}
	
	public abstract AnimationTick getCurrentTick();
	protected abstract void increaseTick();
	public abstract void doTickActions();
	protected void tweakAfterLastTick(){}
	
	public void run(){
		timer.scheduleRepeating(timeStep);
	}
	
	public AnimationTick tick() {
		AnimationTick t = getCurrentTick();
		tickET.invokeListeners(t);
		doTickActions();
		
		if( !t.isLast() ) increaseTick();
		else {
			tweakAfterLastTick();
			stop();
		}
		
		return t;
	}

	public void stop() {
		timer.cancel();
	}

	public void setTimeStepInMillis(int timeStepInMillis){
		timeStep = timeStepInMillis;
	}
	
	public int getTimeStepInMillis() {
		return timeStep;
	}

	public void setWidget(Widget w){
		widget = w;
	}
	
	protected com.google.gwt.dom.client.Element getWidgetElement(){
		if( widget instanceof Dwidget ){
			return ((Dwidget)widget).getFirstInnerElement(); 
		} else {
			return widget.getElement();
		}
	}
	
	public Widget getWidget(){
		return widget;
	}
	
	public void addTickListener(CustomEventListener listener){
		tickET.addCustomEventListener(listener);
	}
	
	public void removeTickListener(CustomEventListener listener) {
		tickET.removeCustomEventListener(listener);
	}

	public void addEndListener(CustomEventListener listener){
		endET.addCustomEventListener(listener);
	}

	public void removeEndListener(CustomEventListener listener) {
		endET.removeCustomEventListener(listener);
	}
	
}
