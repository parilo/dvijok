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

package org.dvijok.db.gwtrpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.dvijok.db.DB_Object;

public interface DataBase_ServiceAsync {

	void Get_Session(AsyncCallback<DB_Object> callback);
	void Auth(DB_Object params, AsyncCallback<DB_Object> callback);
	void Send_Key(DB_Object params, AsyncCallback<DB_Object> callback);
	void Logout(DB_Object params, AsyncCallback<DB_Object> callback);
	void Get_Object(DB_Object params, AsyncCallback<DB_Object> callback);
	void Get_Objects(DB_Object params, AsyncCallback<DB_Object> callback);
	void Put_Object(DB_Object params, AsyncCallback<DB_Object> callback);
	void Del_Object(DB_Object params, AsyncCallback<DB_Object> callback);
	
}
