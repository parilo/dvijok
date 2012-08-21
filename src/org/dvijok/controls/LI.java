package org.dvijok.controls;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LI extends SimplePanel
{
	
	private CustomEventTool overET;
	private CustomEventTool outET;
	
	public LI()
	{
		super((Element) Document.get().createLIElement().cast());
		init();
	}

	public LI(String s)
	{
		this();
		getElement().setInnerText(s);
		init();
	}

	public LI(Widget w)
	{
		this();
		this.add(w);
		init();
	}
	
	private void init(){
		overET = new CustomEventTool();
		outET = new CustomEventTool();
		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
	}
	
	public void addMouseOverListener(CustomEventListener listsner){
		overET.addCustomEventListener(listsner);
	}
	
	public void removeMouseOverListener(CustomEventListener listsner){
		overET.removeCustomEventListener(listsner);
	}
	
	public void addMouseOutListener(CustomEventListener listsner){
		outET.addCustomEventListener(listsner);
	}
	
	public void removeMouseOutListener(CustomEventListener listsner){
		outET.removeCustomEventListener(listsner);
	}
	
	public void onBrowserEvent(Event event)
    {
        switch (DOM.eventGetType(event)){
        case Event.ONMOUSEOVER:
        	overET.invokeListeners(event);
            break;
        case Event.ONMOUSEOUT:
        	outET.invokeListeners(event);
        	break;
        }
    }	
}
