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

import org.dvijok.db.DB_Object;
import org.dvijok.interfaces.DV_Request_Handler;

@RemoteServiceRelativePath("data")
public interface DataBase_Service extends RemoteService{

	DB_Object Get_Session();
	DB_Object Auth(DB_Object params);
	DB_Object Send_Key(DB_Object params);
	DB_Object Logout(DB_Object params);
	DB_Object Get_Object(DB_Object params);
	DB_Object Get_Objects(DB_Object params);
	DB_Object Put_Object(DB_Object params);
	DB_Object Del_Object(DB_Object params);
	
}
