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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.dvijok.db.dvrpc.DVSerializable;

public class DBArray extends ArrayList<Serializable> implements DVSerializable {

	@Override
	public String dvSerialize() {
		String ret = "";
		Iterator<Serializable> i = iterator();
		while( i.hasNext() ){
			Serializable s = i.next();
			if( s instanceof String ){
				String str = (String)s;
				ret += ",STR,"+str.length()+","+str;
			} else if( s instanceof DBObject ){
				DBObject dbo = (DBObject)s;
				String dboser = dbo.dvSerialize();
				ret += ",DBO,"+dboser.length()+","+dboser;
			} else if( s instanceof DBArray ){
				DBArray dba = (DBArray)s;
				String dbaser = dba.dvSerialize();
				ret += ",DBA,"+dbaser.length()+","+dbaser;
			}
		}
		return ret.substring(1);
	}

}
