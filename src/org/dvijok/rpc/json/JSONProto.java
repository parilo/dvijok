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

package org.dvijok.rpc.json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;

import org.dvijok.lib.Lib;
import org.dvijok.rpc.DBArray;
import org.dvijok.rpc.DBObject;
import org.dvijok.rpc.RPCProto;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class JSONProto implements RPCProto {

	public DBObject dboDecode(String indata){
		
		JSONObject obj = JSONParser.parseStrict(indata).isObject();
		if( obj == null ) return null;
		return dboDecode(obj);
		
	}
	
	private DBObject dboDecode(JSONObject obj){
		DBObject ret = new DBObject();
		
		for( String key : obj.keySet() ){
			JSONValue val = obj.get(key);
			if( val.isObject() != null ) ret.put(key, dboDecode(val.isObject()));
			else if( val.isArray() != null ) ret.put(key, dbaDecode(val.isArray()));
			else if( val.isBoolean() != null ) ret.put(key, val.isBoolean().booleanValue()?"1":"0");
			else if( val.isNull() != null ) ret.put(key, "");
			else if( val.isNumber() != null ){
				double v = val.isNumber().doubleValue();
				if( (v == Math.floor(v)) && !Double.isInfinite(v) ){
					int vv = (int) v;
					ret.put(key, ""+vv);
				} else {
					ret.put(key, ""+v);
				}
			}
			else if( val.isString() != null ) ret.put(key, val.isString().stringValue());
		}
		
		return ret;
	}

	public String dboCode(DBObject dbo){
		return dboCode_(dbo).toString();
	}
	
	private JSONObject dboCode_(DBObject dbo){
		JSONObject ret = new JSONObject();
		
		for( String key : dbo.keySet() ){
			Serializable val = dbo.get(key);
			if( val instanceof DBObject ) ret.put(key, dboCode_((DBObject)val));
			else if( val instanceof DBArray ) ret.put(key, dbaCode_((DBArray)val));
			else if( val instanceof String ) ret.put(key, new JSONString((String)val));
		}
		
		return ret;
	}
	
	public DBArray dbaDecode(String indata){
		
		JSONArray arr = JSONParser.parseStrict(indata).isArray();
		if( arr == null ) return null;
		return dbaDecode(arr);
		
	}

	private DBArray dbaDecode(JSONArray arr){
		DBArray ret = new DBArray();
		for( int i=0; i<arr.size(); i++ ){
			JSONValue val = arr.get(i);
			if( val.isObject() != null ) ret.add(dboDecode(val.isObject()));
			else if( val.isArray() != null ) ret.add(dbaDecode(val.isArray()));
			else if( val.isBoolean() != null ) ret.add(val.isBoolean().booleanValue()?"1":"0");
			else if( val.isNull() != null ) ret.add("");
			else if( val.isNumber() != null ){
				double v = val.isNumber().doubleValue();
				if( (v == Math.floor(v)) && !Double.isInfinite(v) ){
					int vv = (int) v;
					ret.add(""+vv);
				} else {
					ret.add(""+v);
				}
			}
			else if( val.isString() != null ) ret.add(val.isString().stringValue());
		}
		return ret;
	}
	
	public String dbaCode(DBArray arr){
		return dbaCode_(arr).toString();
	}
	
	private JSONArray dbaCode_(DBArray arr){
		JSONArray ret = new JSONArray();
		int retindex = 0;
		Iterator<Serializable> i = arr.iterator();
		while( i.hasNext() ){
			Serializable s = i.next();
			if( s instanceof String ) ret.set(retindex++, new JSONString((String)s) );
			else if( s instanceof DBObject ) ret.set(retindex++, dboCode_((DBObject)s) );
			else if( s instanceof DBArray ) ret.set(retindex++, dbaCode_((DBArray)s) );
		}
		return ret;
	}
	
	public String code(Serializable obj){
		if( obj instanceof DBObject ) return dboCode((DBObject)obj);
		else if( obj instanceof DBArray ) return dbaCode((DBArray)obj);
		else return null;
	}
	
	public DBObject decode(String data){
		return dboDecode(data);
	}

	@Override
	public String getName() {
		return "json";
	}
}
