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

import org.dvijok.config.Config;
import org.dvijok.db.DB_Object;
import org.dvijok.db.DataBase;
import org.dvijok.loader.Loader;
import org.dvijok.tmpl.Tmpls_DB;

public class Resources {

	private static Resources self = null;
	
	public Tmpls_DB tmpls;
	public Loader loader;
	public Config conf;
	public DataBase db;
	
	public Resources(){
		this.tmpls = null;
		this.loader = null;
		this.conf = null;
		this.db = null;
	}

	public static Resources getInstance(){
		if(self == null){
			self = new Resources();
		}
		
		return self;
	}
	
}
