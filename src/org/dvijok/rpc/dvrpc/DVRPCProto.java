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

package org.dvijok.rpc.dvrpc;

import java.io.Serializable;
import java.util.Iterator;

import org.dvijok.rpc.DBArray;
import org.dvijok.rpc.DBObject;
import org.dvijok.rpc.RPCProto;

public class DVRPCProto implements RPCProto {

	private int i;
	private String data;
	
	private void setData(String data){
		this.data = data;
		i = 0;
	}
	
	private int extractLen(){
		int pos = data.indexOf(",", i);
		if( pos == -1 ) return -1;
		String lenS = data.substring(i, pos);
		try{
			int len = Integer.parseInt(lenS);
			i = pos+1;
			return len;
		} catch (NumberFormatException e){
			return -1;
		}
	}
	
	private String extract(int len){
		String ret;
		try{
			ret = data.substring(i, i+len);
		} catch ( StringIndexOutOfBoundsException e ){
			return null;
		}
		i += len;
		return ret;
	}
	
	//HashMap
	
	private String dboGetIdent(){
		int len = extractLen();
		if( len == -1 ) return null;
		return extract(len);
	}
	
	private String dboGetType(){
		return extract(3);
	}
	
	private String dboGetVal(){
		return dboGetIdent();	
	}
	
	public DBObject dboDecode(String indata){
		DBObject ret = new DBObject();
		setData(indata);
		String ident;
		String type;
		String val;
		while(
			(ident = dboGetIdent()) != null &&
			(type = dboGetType()) != null &&
			(val = dboGetVal()) != null
		){
			if( type.equals("STR") ){
				ret.put(ident, val);
			} else if( type.equals("DBO") ){
				String currData = data;
				int currI = i;
				ret.put(ident, dboDecode(val));
				data = currData;
				i = currI;
			} else if( type.equals("DBA") ){
				String currData = data;
				int currI = i;
				ret.put(ident, dbaDecode(val));
				data = currData;
				i = currI;
			}
		}
		
		return ret;
	}
	
	public String dboCode(DBObject dbo){
		String ret = "";
		Iterator<String> ki = dbo.keySet().iterator();
		while( ki.hasNext() ){
			String key = ki.next();
			Serializable s = dbo.get(key);
			if( s instanceof String ){
				String str = (String)s;
				ret += key.length()+","+key+"STR"+str.length()+","+str;
			} else if( s instanceof DBObject ){
				String dbos = dboCode((DBObject)s);
				ret += key.length()+","+key+"DBO"+dbos.length()+","+dbos;
			} else if( s instanceof DBArray ){
				String dbas = dbaCode((DBArray)s);
				ret += key.length()+","+key+"DBA"+dbas.length()+","+dbas;
			}
		}
		return ret;
	}
	
	public DBArray dbaDecode(String indata){
		DBArray arr = new DBArray();
		setData(indata);
		String type;
		String val;
		while(
			(type = dboGetType()) != null &&
			(val = dboGetVal()) != null
		){
			if( type.equals("STR") ){
				arr.add(val);
			} else if( type.equals("DBO") ){
				String currData = data;
				int currI = i;
				arr.add(dboDecode(val));
				data = currData;
				i = currI;
			} else if( type.equals("DBA") ){
				String currData = data;
				int currI = i;
				arr.add(dbaDecode(val));
				data = currData;
				i = currI;
			}
		}
		
		return arr;
	}
	
	public String dbaCode(DBArray arr){
		String ret = "";
		Iterator<Serializable> i = arr.iterator();
		while( i.hasNext() ){
			Serializable s = i.next();
			if( s instanceof String ){
				String str = (String)s;
				ret += "STR"+str.length()+","+str;
			} else if( s instanceof DBObject ){
				String dbos = dboCode((DBObject)s);
				ret += "DBO"+dbos.length()+","+dbos;
			} else if( s instanceof DBArray ){
				String dbas = dbaCode((DBArray)s);
				ret += "DBA"+dbas.length()+","+dbas;
			}
		}
		return ret;
	}
	
	public String code(Serializable obj){
		if( obj instanceof DBObject ) return dboCode((DBObject)obj);
		else if( obj instanceof DBArray ) return dbaCode((DBArray)obj);
		else return null;
	}
	
	//can decode only dbo, because request and response in dvrpc is DBObject  
	public DBObject decode(String data){
		return dboDecode(data);
	}

	@Override
	public String getName() {
		return "dvrpc";
	}
}
