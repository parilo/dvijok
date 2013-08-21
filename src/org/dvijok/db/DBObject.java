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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DBObject extends HashMap<String,Serializable> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private DateTimeFormat dtf;

	public String getString(String s){
		String r = (String)this.get(s);
		return r == null?"":r.equals("null")?"":r;
	}
	
	public DBObject getDBObject(String s){
		try{
			return this.containsKey(s)?(DBObject)this.get(s):null;
		} catch( java.lang.ClassCastException e ){
			return null;
		}
	}
	
	public DBArray getDBArray(String s){
		try{
			return this.containsKey(s)?(DBArray)this.get(s):null;
		} catch( java.lang.ClassCastException e ){
			return null;
		}
	}
	
	public double getDouble(String s){
		try{
			return Double.parseDouble(this.getString(s));
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	public int getInt(String s){
		try{
			return Integer.parseInt(this.getString(s));
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	public long getLong(String s){
		try{
			return Long.parseLong(this.getString(s));
		} catch (NumberFormatException e){
			return 0;
		}
	}
	
	public boolean getBoolean(String s){
		return getString(s).equals("1");
	}
	
	public void setBoolean(String s, boolean val){
		put(s, val?"1":"0");
	}
	
	public BigDecimal getBigDecimal(String s){
		try{
			return BigDecimal.valueOf(Double.parseDouble(this.getString(s)));
		} catch (NumberFormatException e){
			return new BigDecimal(0);
		}
	}
	
	public Date getDate(String s){
		if( dtf == null ) dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
		return dtf.parse(getString(s));
	}

	//sort DB_Object inner DB_Objects by string field 
	public DBObject[] getSortedArrayASC(DBObject dbo, final String field){
		return this.getSortedArray(dbo, field, 1);
	}

	public DBObject[] getSortedArrayDESC(DBObject dbo, final String field){
		return this.getSortedArray(dbo, field, -1);
	}
	
	private DBObject[] getSortedArray(DBObject dbo, final String field, final int mult){

		DBObject[] objs = this.getArray();
		
		Arrays.sort(objs, new Comparator<DBObject>(){
			@Override
			public int compare(DBObject o1, DBObject o2) {
				return mult*o1.compareTo(o2, field);
			}
		});
		
		return objs;
	}
		
	protected int compareTo(DBObject o2, String field) {
		return this.getString(field).compareTo(o2.getString(field));
	}

	private DBObject[] getArray(){
		DBObject[] objs = new DBObject[this.keySet().size()];
		Iterator<String> i = this.keySet().iterator();
		int ii=0;
		while( i.hasNext() ){
			objs[ii++] = this.getDBObject(i.next());
		}
		return objs;
	}
	
//	public DBObject copy()

}
