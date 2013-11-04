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

import com.google.gwt.user.client.ui.Widget;

public interface Animation {

	/**
	 * this function must be explictly invoked only once
	 * before perform animation. You may init
	 * internal resources in this function
	 */
	public void init();
	
	/**
	 * invokes reset() than perform ticks
	 */
	public void run();
	
	/**
	 * perform one animation atomic action
	 * @return information about current animation phase
	 */
	public AnimationTick tick();
	
	/**
	 * stops animation
	 */
	public void stop();
	
	/**
	 * must reset internal counters
	 * if needed
	 */
	public void reset();

	public AnimationTick getCurrentTick();
	
	public void setTimeStepInMillis(int timeStepInMillis);
	public int getTimeStepInMillis();
	public void setWidget(Widget w);
	public Widget getWidget();
	
	public void addTickListener(CustomEventListener listener);
	public void removeTickListener(CustomEventListener listener);
	public void addEndListener(CustomEventListener listener);
	public void removeEndListener(CustomEventListener listener);
	
}
