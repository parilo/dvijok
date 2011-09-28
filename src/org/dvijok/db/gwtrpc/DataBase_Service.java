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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import org.dvijok.db.DBObject;
import org.dvijok.interfaces.DVRequestHandler;

@RemoteServiceRelativePath("data")
public interface DataBase_Service extends RemoteService{

	DBObject Get_Session();
	DBObject Auth(DBObject params);
	DBObject Send_Key(DBObject params);
	DBObject Logout(DBObject params);
	DBObject Get_Object(DBObject params);
	DBObject Get_Objects(DBObject params);
	DBObject Put_Object(DBObject params);
	DBObject Del_Object(DBObject params);
	
}
