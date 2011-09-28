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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.dvijok.db.dvrpc.DVDeserializeException;
import org.dvijok.db.dvrpc.DVSerializable;
import org.dvijok.lib.Lib;

public class DBObject extends HashMap<String,Serializable> implements Serializable, DVSerializable {
	
	private static final long serialVersionUID = 1L;

	public String Get_String(String s){
		String r = (String)this.get(s);
		return r == null?"":r.equals("null")?"":r;
	}
	
	public DBObject Get_DB_Object(String s){
		try{
			return this.containsKey(s)?(DBObject)this.get(s):null;
		} catch( java.lang.ClassCastException e ){
			return null;
		}
	}
	
	public double Get_Double(String s){
		try{
			return Double.parseDouble(this.Get_String(s));
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	public int Get_Int(String s){
		try{
			return Integer.parseInt(this.Get_String(s));
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	public long Get_Long(String s){
		try{
			return Long.parseLong(this.Get_String(s));
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	public boolean Get_Boolean(String s){
		return Get_String(s).equals("1");
	}
	
	public BigDecimal Get_BigDecimal(String s){
		try{
			return BigDecimal.valueOf(Double.parseDouble(this.Get_String(s)));
		} catch (NumberFormatException e){
			return new BigDecimal(0);
		}
	}

	//sort DB_Object inner DB_Objects by string field 
	public DBObject[] Get_Sorted_Array_ASC(DBObject dbo, final String field){
		return this.Get_Sorted_Array(dbo, field, 1);
	}

	public DBObject[] Get_Sorted_Array_DESC(DBObject dbo, final String field){
		return this.Get_Sorted_Array(dbo, field, -1);
	}
	
	private DBObject[] Get_Sorted_Array(DBObject dbo, final String field, final int mult){

		DBObject[] objs = this.Get_Array();
		
		Arrays.sort(objs, new Comparator<DBObject>(){
			@Override
			public int compare(DBObject o1, DBObject o2) {
				return mult*o1.compareTo(o2, field);
			}
		});
		
		return objs;
	}
		
	protected int compareTo(DBObject o2, String field) {
		return this.Get_String(field).compareTo(o2.Get_String(field));
	}

	private DBObject[] Get_Array(){
		DBObject[] objs = new DBObject[this.keySet().size()];
		Iterator<String> i = this.keySet().iterator();
		int ii=0;
		while( i.hasNext() ){
			objs[ii++] = this.Get_DB_Object(i.next());
		}
		return objs;
	}

	@Override
	public String dvSerialize() {
		String ret = "";
		Iterator<String> ki = keySet().iterator();
		while( ki.hasNext() ){
			String key = ki.next();
			Serializable s = get(key);
			if( s instanceof String ){
				String str = (String)s;
				ret += key.length()+","+key+"STR"+str.length()+","+str;
			} else if( s instanceof DBObject ){
				DBObject dbo = (DBObject)s;
				String dbos = dbo.dvSerialize();
				ret += key.length()+","+key+"DBO"+dbos.length()+","+dbos;
			} else if( s instanceof DBArray ){
				DBArray dba = (DBArray)s;
				String dbas = dba.dvSerialize();
				ret += key.length()+","+key+"DBA"+dbas.length()+","+dbas;
			}
		}
		return ret;
	}

	@Override
	public void dvDeserialize(String str) throws DVDeserializeException {
		Lib.Alert("DBObject: need deserialize: "+str);
	}

}
