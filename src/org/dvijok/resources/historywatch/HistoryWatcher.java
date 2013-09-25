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

package org.dvijok.resources.historywatch;

import java.util.HashMap;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

public class HistoryWatcher {

	private HashMap<String, CustomEventTool> historyETs;
	private HashMap<String, CustomEventTool> historyBeginsWithETs;
	
	public HistoryWatcher(){
		historyETs = new HashMap<String, CustomEventTool>();
		historyBeginsWithETs = new HashMap<String, CustomEventTool>();
		initHistoryWatching();
	}
	
	private void initHistoryWatching(){

		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String token = event.getValue();

				if( historyETs.containsKey(token) ){
					historyETs.get(token).invokeListeners(token);
				}
				
				for( int i=token.length()-1; i>0; i-- ){
					String tokenpart = token.substring(0, i);
					if( historyBeginsWithETs.containsKey(tokenpart) ){
						String[] strs = new String[2];
						strs[0] = tokenpart;
						strs[1] = token.substring(i);
						historyBeginsWithETs.get(tokenpart).invokeListeners(strs);
						break;
					}
				}
				
			}});
		
	}

	public void addHistoryWatch(String token, CustomEventListener listener){
		if( !historyETs.containsKey(token) ) historyETs.put(token, new CustomEventTool());
		historyETs.get(token).addCustomEventListener(listener);
	}

	public void addHistoryWatch(String[] tokens, CustomEventListener listener){
		for( String token : tokens ) addHistoryWatch(token, listener);
	}
	
	public void removeHistoryWatch(String key, CustomEventListener listener){
		historyETs.get(key).removeCustomEventListener(listener);
	}

	public void addHistoryWatchBeginsWith(String token, CustomEventListener listener){
		if( !historyBeginsWithETs.containsKey(token) ) historyBeginsWithETs.put(token, new CustomEventTool());
		historyBeginsWithETs.get(token).addCustomEventListener(listener);
	}

	public void addHistoryWatchBeginsWith(String[] tokens, CustomEventListener listener){
		for( String token : tokens ) addHistoryWatchBeginsWith(token, listener);
	}
	
	public void removeHistoryWatchBeginsWith(String key, CustomEventListener listener){
		historyBeginsWithETs.get(key).removeCustomEventListener(listener);
	}
	
}