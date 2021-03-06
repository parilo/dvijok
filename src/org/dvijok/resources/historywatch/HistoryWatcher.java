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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

public class HistoryWatcher {

	private HistoryWatcherTool historyETs;
	private HistoryWatcherTool historyBeginsWithETs;
//	private HashMap<String, CustomEventTool> historyETs;
//	private HashMap<String, CustomEventTool> historyBeginsWithETs;
	private ArrayList<String> historyOfTokens;
	
	public HistoryWatcher(){
//		historyETs = new HashMap<String, CustomEventTool>();
//		historyBeginsWithETs = new HashMap<String, CustomEventTool>();
		historyETs = new HistoryWatcherTool();
		historyBeginsWithETs = new HistoryWatcherTool();
		historyOfTokens = new ArrayList<String>();
		initHistoryWatching();
	}
	
	private void initHistoryWatching(){

		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String token = event.getValue();

				historyOfTokens.add(0, token);
				
				if( historyETs.getListeners().containsKey(token) ){
					historyETs.getListeners().get(token).invokeListeners(token);
					return;
				}
				
				for( int i=token.length()-1; i>0; i-- ){
					String tokenpart = token.substring(0, i);
					if( historyBeginsWithETs.getListeners().containsKey(tokenpart) ){
						String[] strs = new String[2];
						strs[0] = tokenpart;
						strs[1] = token.substring(i);
						historyBeginsWithETs.getListeners().get(tokenpart).invokeListeners(strs);
						return;
					}
				}
				
			}});
		
	}

	public void addHistoryWatch(String token, CustomEventListener listener){
//		if( !historyETs.containsKey(token) ) historyETs.put(token, new CustomEventTool());
//		historyETs.get(token).addCustomEventListener(listener);
		historyETs.addHistoryWatch(token, listener);
	}

	public void addHistoryWatch(String[] tokens, CustomEventListener listener){
//		for( String token : tokens ) addHistoryWatch(token, listener);
		historyETs.addHistoryWatch(tokens, listener);
	}
	
	public void removeHistoryWatch(String token, CustomEventListener listener){
//		historyETs.get(token).removeCustomEventListener(listener);
		historyETs.removeHistoryWatch(token, listener);
	}

	public void addHistoryWatchBeginsWith(String token, CustomEventListener listener){
//		if( !historyBeginsWithETs.containsKey(token) ) historyBeginsWithETs.put(token, new CustomEventTool());
//		historyBeginsWithETs.get(token).addCustomEventListener(listener);
		historyBeginsWithETs.addHistoryWatch(token, listener);
	}

	public void addHistoryWatchBeginsWith(String[] tokens, CustomEventListener listener){
//		for( String token : tokens ) addHistoryWatchBeginsWith(token, listener);
		historyBeginsWithETs.addHistoryWatch(tokens, listener);
	}
	
	public void removeHistoryWatchBeginsWith(String token, CustomEventListener listener){
//		historyBeginsWithETs.get(key).removeCustomEventListener(listener);
		historyBeginsWithETs.removeHistoryWatch(token, listener);
	}
	
	public String getTokenPriorTo(List<String> tokens){
		Iterator<String> i = historyOfTokens.iterator();
		while( i.hasNext() ){
			String token = i.next();
			if( !tokens.contains(token) ) return token;
		}
		return null;
	}
	
	public String getCurrentHistoryToken(){
		return History.getToken();
	}
	
	public String getPreviousHistoryToken(){
		if( historyOfTokens.size() > 1  ) return historyOfTokens.get(1);
		else return null;
	}
	
	public void changeHistoryToken(String token){
		changeHistoryToken(token, true);
	}
	
	public void changeHistoryToken(String token, boolean issueEvent){
		Lib.changeHashToken(token, issueEvent);
	}
	
}
