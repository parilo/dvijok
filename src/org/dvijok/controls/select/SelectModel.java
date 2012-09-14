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

package org.dvijok.controls.select;

import java.util.ArrayList;
import java.util.HashMap;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.user.client.ui.ListBox;

public class SelectModel {

	private ArrayList<String> labels;
	private HashMap<String,Integer> valToIdx;
	private HashMap<String, Integer> labelToIdx;
	private HashMap<Integer, DBObject> vals;
	private HashMap<Integer, String> valsStr;
	private HashMap<DBObject,Integer> valDBOToIdx;
	private int selectedIdx = 0;
	private int curIdx = 0;
	
	private CustomEventTool selectedChanged;
	private CustomEventTool itemsChanged;
	
	public SelectModel(){
		labels = new ArrayList<String>();
		valToIdx = new HashMap<String,Integer>();
		valDBOToIdx = new HashMap<DBObject,Integer>();
		vals = new HashMap<Integer,DBObject>();
		valsStr = new HashMap<Integer,String>();
		labelToIdx = new HashMap<String, Integer>();
		selectedChanged = new CustomEventTool();
		itemsChanged = new CustomEventTool();
	}
	
	public void addSelectedChangedListener(CustomEventListener listener){
		selectedChanged.addCustomEventListener(listener);
	}
	
	public void removeSelectedChangedListener(CustomEventListener listener){
		selectedChanged.removeCustomEventListener(listener);
	}
	
	public void addItemsChangedListener(CustomEventListener listener){
		itemsChanged.addCustomEventListener(listener);
	}
	
	public void removeItemsChangedListener(CustomEventListener listener){
		itemsChanged.removeCustomEventListener(listener);
	}
	
	public void setSelectedIndex(int idx){
		if( selectedIdx != idx ){
			selectedIdx = idx;
			selectedChanged.invokeListeners(selectedIdx);
		}
	}
	
	public void setSelectedIndexSilent(int idx){
		if( selectedIdx != idx ){
			selectedIdx = idx;
			itemsChanged.invokeListeners(selectedIdx);
		}
	}
	
	public int getSelectedIndex(){
		return selectedIdx;
	}
	
	public String getValue(int idx){
		return valsStr.get(idx);
	}
	
	public int getItemCount(){
		return labels.size();
	}
	

	
	public void setSelectedVal(String val){
		if(this.valToIdx.containsKey(val))
			this.setSelectedIndex(this.valToIdx.get(val));
	}
	
	public void setSelectedVal(DBObject val){
		this.setSelectedIndex(this.valDBOToIdx.get(val));
	}
	
	public void setSelectedItem(String label){
		if( this.labelToIdx.containsKey(label) ) setSelectedIndex(this.labelToIdx.get(label));
		else if( getItemCount() > 0 ) setSelectedIndex(0);
	}
	

	
	public void setSelectedValSilent(String val){
		if(this.valToIdx.containsKey(val))
			this.setSelectedIndexSilent(this.valToIdx.get(val));
	}
	
	public void setSelectedValSilent(DBObject val){
		this.setSelectedIndexSilent(this.valDBOToIdx.get(val));
	}
	
	public void setSelectedItemSilent(String label){
		if( this.labelToIdx.containsKey(label) ) setSelectedIndexSilent(this.labelToIdx.get(label));
		else if( getItemCount() > 0 ) setSelectedIndexSilent(0);
	}

	
	
	public void addItem(String item){
		labels.add(item);
		curIdx++;
		itemsChanged.invokeListeners();
	}
	
	public ArrayList<String> getLabels(){
		return labels;
	}

	public void addItem(String item, String value) {
		valsStr.put(curIdx, value);
		this.labelToIdx.put(item, curIdx);
		this.valToIdx.put(value, curIdx);
		addItem(item);
	}	

	public void addItem(String item, DBObject value) {
		this.vals.put(curIdx, value);
		this.labelToIdx.put(item, curIdx);
		this.valDBOToIdx.put(value, curIdx);
		addItem(item);
	}
	
	public DBObject getSelectedValue(){
		return this.vals.get(this.getSelectedIndex());
	}
	
	public String getSelectedString(){
		return getValue(this.getSelectedIndex());
	}

	public void clear() {
		labels.clear();
		valToIdx.clear();
		labelToIdx.clear();
		vals.clear();
		valsStr.clear();
		valDBOToIdx.clear();
		selectedIdx = 0;
		curIdx = 0;
	}
	
}
