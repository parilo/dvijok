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

package org.dvijok.db.event;

import org.dvijok.db.DBArray;
import org.dvijok.db.DBObject;
import org.dvijok.db.DataBase;
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.lib.Lib;

/**
 * Class for delivery database events through DataBase class, it means through method used by DataBase
 * @author Pechenko Anton Vladimirovich aka Parilo.   mailto: forpost78 at gmail dot com
 *
 */
public class DataBaseEventsDB implements DataBaseEventsInterface {

	private DataBase db;
	private DBArray allParams;
	private DataBaseEventTool evTool;
	
	public DataBaseEventsDB(DataBase db){
		this.db = db;
		this.evTool = new DataBaseEventTool();
		allParams = new DBArray();
	}
	
	@Override
	public void addEventListener(DBObject params, DataBaseEventListener listener) {
		if( !allParams.contains(params) ){
			allParams.add(params);
			evTool.addDataBaseListener(listener);
			listenForEvents();
		}
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
		db.listenForEvents(dbo, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				listenForEvents();
				
				DBObject event = result.getDBObject("event");
				if( event.getString("type").equals("add") ){
					
					evTool.invokeAddListeners(new DataBaseEvent(event));
					
				} else Lib.alert("DataBaseEventsDB: other event: "+result);
				
			}

			@Override
			public void fail(DBObject result) {
			}});
	}
	
}
