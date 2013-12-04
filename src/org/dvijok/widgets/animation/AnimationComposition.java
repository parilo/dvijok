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

import java.util.ArrayList;
import java.util.Iterator;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class AnimationComposition extends AnimationImpl {

	public class AnimationStarter {
		
		private Animation animationToWatch;
		private AnimationTick startTick;
		private Animation animationToStart;
		
		public void setWatch(Animation animationToWatch, AnimationTick startTick){
			this.animationToWatch = animationToWatch;
			this.startTick = startTick;
		}
		
		public void setStart(Animation animationToStart){
			this.animationToStart = animationToStart;
		}
		
		public boolean check(){
			AnimationTick tick = animationToWatch.getCurrentTick();
//if( animations.size() > 2 ) System.out.println("tick: "+tick.getStep()+" : "+tick.getStepInPercent()+" : "+tick.CompareTo(startTick));			
			if( tick.CompareTo(startTick) >= 0 ){
				runningAnimations.add(animationToStart);
				return true;
			}
			return false;
		}
	}
	
	
	
	private int animationStep;
	
	private ArrayList<Animation> animations;
	private ArrayList<Animation> runningAnimations;
	private ArrayList<AnimationStarter> allAnimationStarters;
	private ArrayList<AnimationStarter> watchingAnimationStarters;
	private ArrayList<AnimationStarter> toRemoveFromWatchingAnimationStarters;
	
	public AnimationComposition(){
		animations = new ArrayList<Animation>();
		runningAnimations = new ArrayList<Animation>();
		allAnimationStarters = new ArrayList<AnimationStarter>();
		watchingAnimationStarters = new ArrayList<AnimationStarter>();
		toRemoveFromWatchingAnimationStarters = new ArrayList<AnimationStarter>();
	}
	
	public void addAnimationParallelToLast(Animation a){
		AddAnimationRelativeToLast(a, new AnimationTick((Integer)0));
	}
	
	public void AddAnimationRelativeToLast(Animation a, AnimationTick startTick){
		if( animations.size() < 1 ){
			animations.add(a);
			return;
		}
		
		Animation prevAnimation = animations.get(animations.size()-1);
		AnimationStarter s = new AnimationStarter();
		s.setWatch(prevAnimation, startTick);
		s.setStart(a);
		allAnimationStarters.add(s);
		
		animations.add(a);
	}
	
	@Override
	public void reset() {
		
		animationStep = 1;
		
		runningAnimations.clear();
		if( animations.size() > 0 ){
			runningAnimations.add(animations.get(0));
		}
		
		Iterator<Animation> ai = animations.iterator();
		while( ai.hasNext() ){
			ai.next().reset();
		}
		
		watchingAnimationStarters.clear();
		Iterator<AnimationStarter> i = allAnimationStarters.iterator();
		while( i.hasNext() ){
			watchingAnimationStarters.add(i.next());
		}
	}

	@Override
	public void doTickActions() {
		checkAnimationStarters();
		
		for(int i=0; i<runningAnimations.size(); i++){
			Animation a = runningAnimations.get(i);
			AnimationTick t = a.tick();
			if( t.isLast() ){
				runningAnimations.remove(a);
				i--;
			}
		}
	}
	
	private void checkAnimationStarters(){
		toRemoveFromWatchingAnimationStarters.clear();
		Iterator<AnimationStarter> i = watchingAnimationStarters.iterator();
		while( i.hasNext() ){
			AnimationStarter as = i.next();
			if( as.check() ) toRemoveFromWatchingAnimationStarters.add(as);
		}
		watchingAnimationStarters.removeAll(toRemoveFromWatchingAnimationStarters);
	}

	@Override
	public AnimationTick getCurrentTick() {
		return new AnimationTick(
				getTimeStepInMillis(),
				animationStep,
				0,
				runningAnimations.size()<1?true:false
			);
	}

	@Override
	protected void increaseTick() {
		animationStep++;
	}

	public void setStepCount(int stepCount) {
		Iterator<Animation> ai = animations.iterator();
		while( ai.hasNext() ){
			Animation a = ai.next();
			if( a instanceof HasStepCount ){
				((HasStepCount) a).setStepCount(stepCount);
			}
		}
	}
	
}
