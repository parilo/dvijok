//    Copyright (C) 2010  Pechenko Anton Vladimirovich aka Parilo

package org.dvijok.db;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class DB_Object extends HashMap<String,Serializable> implements Serializable {
	
	public String Get_String(String s){
		String r = (String)this.get(s);
		return r.equals("null")?"":r;
	}
	
	public DB_Object Get_DB_Object(String s){
		try{
			return this.containsKey(s)?(DB_Object)this.get(s):null;
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

	//sort DB_Object inner DB_Objects by string field 
	public DB_Object[] Get_Sorted_Array_ASC(DB_Object dbo, final String field){
		return this.Get_Sorted_Array(dbo, field, 1);
	}

	public DB_Object[] Get_Sorted_Array_DESC(DB_Object dbo, final String field){
		return this.Get_Sorted_Array(dbo, field, -1);
	}
	
	private DB_Object[] Get_Sorted_Array(DB_Object dbo, final String field, final int mult){

		DB_Object[] objs = this.Get_Array();
		
		Arrays.sort(objs, new Comparator<DB_Object>(){
			@Override
			public int compare(DB_Object o1, DB_Object o2) {
				return mult*o1.compareTo(o2, field);
			}
		});
		
		return objs;
	}
		
	protected int compareTo(DB_Object o2, String field) {
		return this.Get_String(field).compareTo(o2.Get_String(field));
	}

	private DB_Object[] Get_Array(){
		DB_Object[] objs = new DB_Object[this.keySet().size()];
		Iterator<String> i = this.keySet().iterator();
		int ii=0;
		while( i.hasNext() ){
			objs[ii++] = this.Get_DB_Object(i.next());
		}
		return objs;
	}

}
