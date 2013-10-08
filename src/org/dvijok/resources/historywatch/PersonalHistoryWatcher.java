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
import java.util.Iterator;
import java.util.List;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

public class PersonalHistoryWatcher {
	
	private HistoryWatcher historyWatcher;
	private HistoryWatcherTool listeners;
	private HistoryWatcherTool listenersBeginWith;
	
	public PersonalHistoryWatcher(){
		this.historyWatcher = Resources.getInstance().historyWatcher;
		init();
	}
	
	public PersonalHistoryWatcher(HistoryWatcher historyWatcher){
		this.historyWatcher = historyWatcher;
		init();
	}
	
	private void init(){
		listeners = new HistoryWatcherTool();
		listenersBeginWith = new HistoryWatcherTool();
	}
	
	public void stopListening(){
		HashMap<String, CustomEventTool> ls = listeners.getListeners();
		Iterator<String> i = ls.keySet().iterator();
		while( i.hasNext() ){
			String token = i.next();
			Iterator<CustomEventListener> ii = ls.get(token).iterator();
			while( ii.hasNext() ){
				historyWatcher.removeHistoryWatch(token, ii.next());
			}
		}
		
		ls = listenersBeginWith.getListeners();
		i = ls.keySet().iterator();
		while( i.hasNext() ){
			String token = i.next();
			Iterator<CustomEventListener> ii = ls.get(token).iterator();
			while( ii.hasNext() ){
				historyWatcher.removeHistoryWatchBeginsWith(token, ii.next());
			}
		}
	}
	
	public void startListening(){
		HashMap<String, CustomEventTool> ls = listeners.getListeners();
		Iterator<String> i = ls.keySet().iterator();
		while( i.hasNext() ){
			String token = i.next();
			Iterator<CustomEventListener> ii = ls.get(token).iterator();
			while( ii.hasNext() ){
				historyWatcher.addHistoryWatch(token, ii.next());
			}
		}
		
		ls = listenersBeginWith.getListeners();
		i = ls.keySet().iterator();
		while( i.hasNext() ){
			String token = i.next();
			Iterator<CustomEventListener> ii = ls.get(token).iterator();
			while( ii.hasNext() ){
				historyWatcher.addHistoryWatchBeginsWith(token, ii.next());
			}
		}
	}
	
	public void fireCurrentHistoryToken(){
		fireHistoryToken(getCurrentHistoryToken());
	}
	
	public void fireHistoryToken(String token){
		if( listeners.getListeners().containsKey(token) ){
			listeners.getListeners().get(token).invokeListeners(token);
			return;
		}
		
		for( int i=token.length()-1; i>0; i-- ){
			String tokenpart = token.substring(0, i);
			if( listenersBeginWith.getListeners().containsKey(tokenpart) ){
				String[] strs = new String[2];
				strs[0] = tokenpart;
				strs[1] = token.substring(i);
				listenersBeginWith.getListeners().get(tokenpart).invokeListeners(strs);
				return;
			}
		}
		
	}

	public void addHistoryWatch(String token, CustomEventListener listener){
		historyWatcher.addHistoryWatch(token, listener);
		listeners.addHistoryWatch(token, listener);
	}

	public void addHistoryWatch(String[] tokens, CustomEventListener listener){
		historyWatcher.addHistoryWatch(tokens, listener);
		listeners.addHistoryWatch(tokens, listener);
	}
	
	public void removeHistoryWatch(String token, CustomEventListener listener){
		historyWatcher.removeHistoryWatch(token, listener);
		listeners.removeHistoryWatch(token, listener);
	}

	public void addHistoryWatchBeginsWith(String token, CustomEventListener listener){
		historyWatcher.addHistoryWatchBeginsWith(token, listener);
		listenersBeginWith.addHistoryWatch(token, listener);
	}

	public void addHistoryWatchBeginsWith(String[] tokens, CustomEventListener listener){
		historyWatcher.addHistoryWatchBeginsWith(tokens, listener);
		listenersBeginWith.addHistoryWatch(tokens, listener);
	}
	
	public void removeHistoryWatchBeginsWith(String key, CustomEventListener listener){
		historyWatcher.removeHistoryWatchBeginsWith(key, listener);
		listenersBeginWith.removeHistoryWatch(key, listener);
	}
	
	public String getTokenPriorTo(List<String> tokens){
		return historyWatcher.getTokenPriorTo(tokens);
	}
	
	public String getCurrentHistoryToken(){
		return historyWatcher.getCurrentHistoryToken();
	}
	
	public String getPreviousHistoryToken(){
		return historyWatcher.getPreviousHistoryToken();
	}
	
	public void changeHistoryToken(String token){
		historyWatcher.changeHistoryToken(token);
	}
	
	public void changeHistoryToken(String token, boolean issueEvent){
		historyWatcher.changeHistoryToken(token, issueEvent);
	}
	
}
