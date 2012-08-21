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

package org.dvijok.resources;

import org.dvijok.db.DBObject;
import org.dvijok.handlers.DVRequestHandler;

public class AuthTool {

	public void logout(){
		logout(null);
	}
	
	public void logout( final DVRequestHandler<DBObject> onLogout ){
		
		Resources.getInstance().db.logout(null, new DVRequestHandler<DBObject>(){
	
			@Override
			public void success(DBObject result) {
				Resources.getInstance().onAuth(new DBObject());
				if( onLogout != null ) onLogout.success(result);
			}
	
			@Override
			public void fail(DBObject result) {
				onLogout.fail(result);
			}});
	
	}
	
	
}
