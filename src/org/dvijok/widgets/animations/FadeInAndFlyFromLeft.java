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

package org.dvijok.widgets.animations;

import org.dvijok.widgets.animation.AnimationComposition;
import org.dvijok.widgets.animation.AnimationImpl;
import org.dvijok.widgets.animation.AnimationTick;
import org.dvijok.widgets.animation.HasStepCount;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class FadeInAndFlyFromLeft extends AnimationComposition implements HasStepCount {
	
	private FadeIn fadeIn;
	private FlyFromLeft flyFromLeft;
	
	public FadeInAndFlyFromLeft(){
		super();
		
		fadeIn = new FadeIn();
		flyFromLeft = new FlyFromLeft();
		setStepCount(fadeIn.getStepCount());
		
		addAnimationParallelToLast(fadeIn);
		addAnimationParallelToLast(flyFromLeft);
	}

	@Override
	public void setWidget(Widget w) {
		fadeIn.setWidget(w);
		flyFromLeft.setWidget(w);
		super.setWidget(w);
	}

	@Override
	public void setStepCount(int stepCount) {
		fadeIn.setStepCount(stepCount);
		flyFromLeft.setStepCount(stepCount);
	}

	@Override
	public int getStepCount() {
		return fadeIn.getStepCount();
	}

	@Override
	public AnimationTick getCurrentTick() {
		return fadeIn.getCurrentTick();
	}
	
}
