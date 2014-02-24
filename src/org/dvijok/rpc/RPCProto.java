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

import java.io.Serializable;

import org.dvijok.rpc.DBArray;
import org.dvijok.rpc.DBObject;

public interface RPCProto {
	
	public DBObject dboDecode(String indata);
	public String dboCode(DBObject dbo);
	
	public DBArray dbaDecode(String indata);
	public String dbaCode(DBArray arr);

	/**
	 * can code DBObject or DBArray
	 * @param DBObject or DBArray
	 * @return string encoded object 
	 */
	public String code(Serializable obj);
	
	/**
	 * can decode only DBObject representation
	 * @param string encoded DBObject
	 * @return 
	 */
	public DBObject decode(String data);
	
	public String getName();
}