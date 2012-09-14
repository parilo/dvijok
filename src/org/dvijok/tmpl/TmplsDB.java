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

package org.dvijok.tmpl;

import java.util.HashMap;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.HttpFunctions;
import org.dvijok.resources.Resources;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class TmplsDB {

	private HashMap<String,String> tmpls;
	private HashMap<String, CustomEventTool> waiters;
	
	public TmplsDB(){
		this.tmpls = new HashMap<String,String>();
		waiters = new HashMap<String, CustomEventTool>();
	}
	
	public void getTemplate(final String url, final CustomEventListener req){
		
		if( this.tmpls.containsKey(url) ){
			req.customEventOccurred(new CustomEvent(this.tmpls.get(url)));
		} else {

			if( waiters.containsKey(url) ){
				waiters.get(url).addCustomEventListener(req);
			} else {
			
				waiters.put(url, new CustomEventTool());
				
	//			Resources.getInstance().db.pauseListenForEvents();
				HttpFunctions.doGet(url, new RequestCallback(){
		
					@Override
					public void onError(Request request, Throwable exception) {
						CustomEvent ev = new CustomEvent(exception.getMessage());
						ev.setFailed(true);
						req.customEventOccurred(ev);
						if( waiters.containsKey(url) ) waiters.get(url).invokeListeners(ev);
					}
		
					@Override
					public void onResponseReceived(Request request, Response response) {
						String text = response.getText();
						tmpls.put(url, text);
						req.customEventOccurred(new CustomEvent(text));
						if( waiters.containsKey(url) ) waiters.get(url).invokeListeners(text);
					}
					
				});
	//			Resources.getInstance().db.resumeListenForEvents();
			
			}

		}
	}

	
	
	
	
	
	
}
