//    dvijok - cms written in gwt
//    Copyright (C) 2010-2012  Pechenko Anton Vladimirovich aka Parilo
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
