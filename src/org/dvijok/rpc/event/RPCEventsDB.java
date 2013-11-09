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

package org.dvijok.rpc.event;

import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.rpc.DBArray;
import org.dvijok.rpc.DBObject;
import org.dvijok.rpc.RPC;

/**
 * Class for delivery database events through DataBase class, it means through method used by DataBase
 * @author Pechenko Anton Vladimirovich aka Parilo.   mailto: forpost78 at gmail dououououot com
 *
 */
public class RPCEventsDB implements DataBaseEventsInterface {

	private RPC db;
	private DBArray allParams;
	private DataBaseEventTool evTool;
	private String queueid = "";
	
	public RPCEventsDB(RPC db){
		this.db = db;
		this.evTool = new DataBaseEventTool();
		allParams = new DBArray();
	}
	
	@Override
	public void addEventListener(DBObject params, DataBaseEventListener listener) {
		listener.setTagsArray(params.getString("tags"));
		if( !allParams.contains(params) ){
			allParams.add(params);
		}
		evTool.addDataBaseListener(listener);
		db.stopListenForEvents();
		listenForEvents();
	}

	@Override
	public void removeEventListener(DBObject params, DataBaseEventListener listener) {
		
		if( allParams.contains(params) ){
			allParams.remove(params);
			evTool.removeDataBaseListener(listener);
			
			if( allParams.size() > 0 ){
				listenForEvents();
			} else {
				db.stopListenForEvents();
			}
			
		}
	}

	private void listenForEvents() {
		DBObject dbo = new DBObject();
		dbo.put("need", allParams);
		if( !queueid.equals("") ) dbo.put("qid", queueid);
		db.listenForEvents(dbo, new RequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				
				DBObject event = result.getDBObject("event");
				
				if( event.containsKey("qid") ){
					queueid = event.getString("qid");
				}
				
//				if( event.getString("type").equals("add") ){
//					
//					evTool.invokeAddListeners(new DataBaseEvent(event));
//					
//				} else if( event.getString("type").equals("mod") ){
//					
//					evTool.invokeModifyListeners(new DataBaseEvent(event));
//					
//				} else if( event.getString("type").equals("del") ){
//					
//					evTool.invokeDelListeners(new DataBaseEvent(event));
//					
//				} else
				
				if( event.getString("type").equals("idle") ){
				} else {
					
					evTool.invokeListeners(new DataBaseEvent(event));
					
				}
				
				//Lib.alert("DataBaseEventsDB: other event: "+result);
				
				listenForEvents();
			}

			@Override
			public void fail(DBObject result) {
			}});
	}
	
}
