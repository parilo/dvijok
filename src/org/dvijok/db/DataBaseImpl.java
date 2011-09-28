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

package org.dvijok.db;

import org.dvijok.db.dvrpc.DBRequestDVRPC;
import org.dvijok.interfaces.DVRequestHandler;
import org.dvijok.lib.Lib;

public class DataBaseImpl implements DataBase {

	private DBRequest dbRequest;
	
	public DataBaseImpl(){
		dbRequest = new DBRequestDVRPC("http://127.0.0.1:8888/dvrpc/dvrpc.php");
		
		DBObject dbo = new DBObject();
		dbo.put("func", "initSession");
		dbo.put("aaa", "1234");
		dbo.put("bbbbb", "123");
		initSession(dbo, null);
	}
	
	private void initSession(DBObject params, DVRequestHandler<DBObject> handler) {
		dbRequest.request(params, new DVRequestHandler<DBObject>(){

			@Override
			public void success(DBObject result) {
				Lib.Alert("DataBaseDVRPC success: "+result);
			}

			@Override
			public void fail(DBObject result) {
				Lib.Alert("DataBaseDVRPC fail: "+result);
			}});
	}

	@Override
	public void auth(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void sendKey(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void logout(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void getObject(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void getObjects(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void putObject(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void delObject(DBObject params, DVRequestHandler<DBObject> handler) {
	}

	@Override
	public void listenForEvents(DBObject params, DVRequestHandler<DBObject> handler) {
	}

}
