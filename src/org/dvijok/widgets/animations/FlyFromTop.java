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

import org.dvijok.widgets.animation.AnimationImpl;
import org.dvijok.widgets.animation.AnimationTick;
import org.dvijok.widgets.animation.HasStepCount;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;

public class FlyFromTop extends AnimationImpl implements HasStepCount {

	private int animationStep;
	private int animationStepCount;
	
	private double targetY;
	
	private double startY;
	private double startV;
	private double accel;
	
	private double currentY;
	
	private String storedCSSPosition;
	private String storedCSSTop;
	
	public FlyFromTop(){
		animationStepCount = 40;
	}
	
	@Override
	public void reset() {
		animationStep = 1;
		
		//target X, X that element must have at the end of movement
		targetY = getWidget().getElement().getAbsoluteTop();
		startY = 0-getWidget().getOffsetHeight();
		
		startV = 1.*(targetY-startY)/animationStepCount*2;
		accel = -1.*startV/animationStepCount;

		storedCSSPosition = getWidgetElement().getStyle().getPosition();
		storedCSSTop = getWidgetElement().getStyle().getTop();
		getWidgetElement().getStyle().setPosition(Position.RELATIVE);
		getWidgetElement().getStyle().setLeft(startY, Unit.PX);
	}

	@Override
	protected void tweakAfterLastTick() {
		getWidgetElement().getStyle().setProperty("position", storedCSSPosition);
		getWidgetElement().getStyle().setProperty("top", storedCSSTop);
		super.tweakAfterLastTick();
	}

	@Override
	public void doTickActions() {
		currentY = startY + startV*animationStep + accel*animationStep*animationStep/2;
		getWidgetElement().getStyle().setTop(currentY, Unit.PX);
	}

	@Override
	public AnimationTick getCurrentTick() {
		return new AnimationTick(
							getTimeStepInMillis(),
							animationStep,
							1.*animationStep/animationStepCount*100,
							animationStep==animationStepCount?true:false
						);
	}

	@Override
	protected void increaseTick() {
		animationStep++;
	}

	@Override
	public void setStepCount(int stepCount) {
		animationStepCount = stepCount;
	}

	@Override
	public int getStepCount() {
		return animationStepCount;
	}
	
}
