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

package org.dvijok.rpc;

import org.dvijok.handlers.RequestHandler;
import org.dvijok.rpc.event.DataBaseEventListener;

public interface RPC {

	public String getSid();
	public void checkSession(DBObject params, final RequestHandler<DBObject> handler);
	public void login(DBObject params, final RequestHandler<DBObject> handler);
	public void sendLoginKey(DBObject params, final RequestHandler<DBObject> handler);
	public void logout(DBObject params, final RequestHandler<DBObject> handler);
	public void saveUserData(DBObject userData, final RequestHandler<DBObject> handler);
	public void getTemplatesCache(final RequestHandler<DBObject> handler);
	public void external(DBObject params, final RequestHandler<DBObject> handler);
	
	public void addEventListener(DBObject params, DataBaseEventListener listener);
	public void removeEventListener(DBObject params, DataBaseEventListener listener);
	public void listenForEvents(DBObject params, final RequestHandler<DBObject> handler);
	public void stopListenForEvents();
	public void pauseListenForEvents();
	public void resumeListenForEvents();
	
}
