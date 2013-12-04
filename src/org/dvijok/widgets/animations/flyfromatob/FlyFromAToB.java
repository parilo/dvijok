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

package org.dvijok.widgets.animations.flyfromatob;

import org.dvijok.widgets.animation.AnimationImpl;
import org.dvijok.widgets.animation.AnimationTick;
import org.dvijok.widgets.animation.HasStepCount;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class FlyFromAToB extends AnimationImpl implements HasStepCount {

	private int animationStep;
	private int animationStepCount;

	private double targetX;
	private double targetY;
	
	private double startX;
	private double startY;
	
	private double startVx;
	private double startVy;
	private double accelX;
	private double accelY;
	
	private double currentX;
	private double currentY;
	
	private String storedCSSPosition;
	private String storedCSSLeft;
	
	public FlyFromAToB(){
		animationStepCount = 40;
	}
	
	public void setAState(Widget w){
		startX = w.getElement().getAbsoluteLeft();
		startY = w.getElement().getAbsoluteTop();
	}
	
	public void setBState(Widget w){
		targetX = w.getElement().getAbsoluteLeft();
		targetY = w.getElement().getAbsoluteTop();
	}
	
	public double getAStateLeft(){
		return startX;
	}
	
	public double getAStateTop(){
		return startY;
	}
	
	@Override
	public void reset() {
		animationStep = 1;
		
		//target X, X that element must have at the end of movement
//		targetX = getWidget().getElement().getAbsoluteLeft();
//		startX = Window.getClientWidth();
		
		startVx = 1.*(targetX-startX)/(animationStepCount-1)*2;
		startVy = 1.*(targetY-startY)/(animationStepCount-1)*2;
		accelX = -1.*startVx/(animationStepCount-1);
		accelY = -1.*startVy/(animationStepCount-1);

//		storedCSSPosition = getWidgetElement().getStyle().getPosition();
//		getWidgetElement().getStyle().setPosition(Position.ABSOLUTE);
//		RootPanel.get().add(getWidget());
//		storedCSSLeft = getWidgetElement().getStyle().getLeft();
//		getWidgetElement().getStyle().setPosition(Position.RELATIVE);
//		getWidgetElement().getStyle().setLeft(startX, Unit.PX);
	}

//	@Override
//	protected void tweakAfterLastTick() {
//		getWidgetElement().getStyle().setProperty("position", storedCSSPosition);
//		getWidgetElement().getStyle().setProperty("left", storedCSSLeft);
//		super.tweakAfterLastTick();
//	}

	@Override
	public void doTickActions() {
		currentX = startX + startVx*animationStep + accelX*animationStep*animationStep/2;
		currentY = startY + startVy*animationStep + accelY*animationStep*animationStep/2;
		getWidgetElement().getStyle().setLeft(currentX, Unit.PX);
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
