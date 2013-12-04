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

import org.dvijok.lib.Lib;
import org.dvijok.widgets.animation.AnimationImpl;
import org.dvijok.widgets.animation.AnimationTick;
import org.dvijok.widgets.animation.HasStepCount;

public class FadeIn extends AnimationImpl implements HasStepCount {

	private int animationStep;
	private int animationStepCount;
	private double currentOpacity;
	
	public FadeIn(){
		animationStepCount = 40;
	}
	
	@Override
	public void reset() {
		animationStep = 1;
		currentOpacity = 0;
		getWidgetElement().getStyle().setOpacity(0);
	}

	@Override
	public void doTickActions() {
		currentOpacity = 1.*animationStep/animationStepCount;
		getWidgetElement().getStyle().setOpacity(currentOpacity);
	}

	@Override
	protected void tweakAfterLastTick() {
		getWidgetElement().getStyle().setOpacity(1);
		super.tweakAfterLastTick();
	}

	@Override
	public AnimationTick getCurrentTick() {
		return new AnimationTick(
							getTimeStepInMillis(),
							animationStep,
							currentOpacity*100,
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
