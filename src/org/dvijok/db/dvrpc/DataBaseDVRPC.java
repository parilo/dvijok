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

package org.dvijok.db.dvrpc;

import org.dvijok.db.DBObject;
import org.dvijok.db.DataBase;
import org.dvijok.interfaces.DVRequestHandler;

public class DataBaseDVRPC implements DataBase {

	@Override
	public void initSession(DBObject params, DVRequestHandler<DBObject> handler) {
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
