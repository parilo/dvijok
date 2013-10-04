// author: Pechenko Anton Vladimirovich. 2012

package org.dvijok.lib;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.user.client.Timer;

public class LasyInvoke {

	private Timer timer;
	private int delay;
	private Object object;
	
	private CustomEventTool invokeET;
	
	public LasyInvoke(){
		delay = 1500;
		timer = new Timer(){
			@Override
			public void run() {
				realInvoke();
			}
		};
		invokeET = new CustomEventTool();
	}
	
	public void invoke(){
		timer.cancel();
		timer.schedule(delay); 
	}
	
	public void cancel(){
		timer.cancel();
	}
	
	private void realInvoke(){
		invokeET.invokeListeners(this);
	}
	
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delayInMillis) {
		this.delay = delayInMillis;
	}

	public void addInvokeListener(CustomEventListener listener){
		invokeET.addCustomEventListener(listener);
	}
	
	public void removeInvokeListener(CustomEventListener listener){
		invokeET.removeCustomEventListener(listener);
	}
	
}
