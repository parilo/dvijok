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

package org.dvijok.controls;

import java.util.HashMap;

import org.dvijok.db.DBObject;

import com.google.gwt.user.client.ui.ListBox;

public class Select extends ListBox {

	private HashMap<String,Integer> valToIdx;
	private HashMap<String, Integer> labelToIdx;
	private HashMap<Integer,DBObject> vals;
	private HashMap<DBObject,Integer> valDBOToIdx;
	private int curIdx = 0;
	
	public Select(){
		valToIdx = new HashMap<String,Integer>();
		valDBOToIdx = new HashMap<DBObject,Integer>();
		vals = new HashMap<Integer,DBObject>();
		labelToIdx = new HashMap<String, Integer>();
	}
	
	public void setSelectedVal(String val){
		if(this.valToIdx.containsKey(val))
			this.setSelectedIndex(this.valToIdx.get(val));
	}
	
	public void setSelectedVal(DBObject val){
		this.setSelectedIndex(this.valDBOToIdx.get(val));
	}

	@Override
	public void addItem(String item, String value) {
		this.labelToIdx.put(item, curIdx);
		this.valToIdx.put(value, curIdx++);
		super.addItem(item, value);
	}	

	public void addItem(String item, DBObject value) {
		this.vals.put(curIdx, value);
		this.labelToIdx.put(item, curIdx);
		this.valDBOToIdx.put(value, curIdx++);
		super.addItem(item);
	}
	
	public DBObject getSelectedValue(){
		return this.vals.get(this.getSelectedIndex());
	}
	
	public String getSelectedString(){
		return this.getValue(this.getSelectedIndex());
	}
	
	public void setSelectedItem(String label){
		if( this.labelToIdx.containsKey(label) ) super.setSelectedIndex(this.labelToIdx.get(label));
		else if( this.getItemCount() > 0 ) super.setSelectedIndex(0);
	}

	@Override
	public void clear() {
		vals.clear();
		valToIdx.clear();
		valDBOToIdx.clear();
		curIdx = 0;
		super.clear();
	}
	
}
