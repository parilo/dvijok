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

public class AnimationTick {

	private int timeStep; //in millis
	private int step; // number of current tick in tick sequence
	private double stepInPercent; // percent shows how much progress done in tick sequence with current tick
	private boolean isLast;

	public AnimationTick() {
		this.timeStep = 100;
		this.step = 0;
		this.stepInPercent = 0;
		this.isLast = false;
	}

	public AnimationTick(int step) {
		this.timeStep = 100;
		this.step = step;
		this.stepInPercent = 0;
		this.isLast = false;
	}

	public AnimationTick(double stepInPercent) {
		this.timeStep = 100;
		this.step = 0;
		this.stepInPercent = stepInPercent;
		this.isLast = false;
	}

	public AnimationTick(int timeStep, int step, double stepInPercent,
			boolean isLast) {
		super();
		this.timeStep = timeStep;
		this.step = step;
		this.stepInPercent = stepInPercent;
		this.isLast = isLast;
	}

	public int getTimeStep() {
		return timeStep;
	}

	public void setTimeStepInMillis(int timeStep) {
		this.timeStep = timeStep;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public double getStepInPercent() {
		return stepInPercent;
	}

	public void setStepInPercent(double stepInPercent) {
		this.stepInPercent = stepInPercent;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	
	public double CompareTo(AnimationTick a){
		if( a.getStepInPercent() > 0 ){
			//comparing by percent
			return getStepInPercent() - a.getStepInPercent();
		} else {
			//comparing by step
			return getStep() - a.getStep();
		}
	}
}
