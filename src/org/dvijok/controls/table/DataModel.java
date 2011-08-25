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

package org.dvijok.controls.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.user.client.ui.Widget;

public class DataModel {
	
	private ArrayList<Object> data;
	private ArrayList<Object> sortdata;
	private DataModel styles = null;
	private CustomEventTool addedET;
	private CustomEventTool changedET;
	private CustomEventTool removedET;
	private boolean sortable = true;
	private ArrayList<Integer> colSpan;
	private ArrayList<Integer> rowSpan;
	
	public DataModel(){
		this.data = new ArrayList<Object>();
		this.sortdata = new ArrayList<Object>();
		this.addedET = new CustomEventTool();
		this.changedET = new CustomEventTool();
		this.removedET = new CustomEventTool();
		this.colSpan = new ArrayList<Integer>();
		this.rowSpan = new ArrayList<Integer>();
	}
	
	public Iterator<Object> getIterator(){
		return data.iterator();
	}
	
	public Object get(int index){
		return this.data.get(index);
	}
	
	public boolean isHeaderCellInfo(int index){
		return data.get(index) instanceof HeaderCellInfo;
	}
	
	public HeaderCellInfo getHeaderCellInfo(int index){
		return (HeaderCellInfo) data.get(index);
	}
	
	public String getString(int index){
		return (String)this.data.get(index);
	}
	
	public int getInt(int index){
		return (Integer)this.data.get(index);
	}
	
	public Widget getWidget(int index){
		return (Widget) this.data.get(index);
	}
	
	public boolean isWidget(int index){
		Object o = this.data.get(index);
		if( o instanceof Widget ) return true;
		else return false;
	}
	
	//for sorting
	public Object getSort(int index){
		return this.sortdata.get(index);
	}
	
	public String getStringSort(int index){
		return (String)this.sortdata.get(index);
	}
	
	public int getIntSort(int index){
		return (Integer)this.sortdata.get(index);
	}
	
	public Widget getWidgetSort(int index){
		return (Widget) this.sortdata.get(index);
	}
	
	public int getSize(){
		return this.data.size();
	}
	
	public void addSortData(Object o){
		this.sortdata.add(o);
		this.data.add(null);
		this.colSpan.add(1);
		this.rowSpan.add(1);
	}
	
	public void add(Object o){
		add(o,1,1);
	}
	
	public void add(Object o, int colspan, int rowspan){
		int ind = this.data.size();
		this.data.add(o);
		this.sortdata.add(o);
		this.addedET.invokeListeners(ind);
		this.colSpan.add(colspan);
		this.rowSpan.add(rowspan);
	}
	
	public void remove(Object o){
		int ind = this.data.indexOf(o);
		this.data.remove(o);
		this.removedET.invokeListeners(ind);
	}
	
	public void set(int index, Object o){
		this.data.set(index, o);
		this.changedET.invokeListeners(index);
	}
	
	public DataModel getStyles() {
		return styles;
	}

	public void setStyles(DataModel styles) {
		this.styles = styles;
	}
	
	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}
	
	public int getColSpan(int index) {
		return colSpan.get(index);
	}

	public void setColSpan(int colSpan, int index) {
		this.colSpan.set(index, colSpan);
	}

	public int getRowSpan(int index) {
		return rowSpan.get(index);
	}

	public void setRowSpan(int rowSpan, int index) {
		this.rowSpan.set(index, rowSpan);
	}

	public void addAddedListener(CustomEventListener listener){
		this.addedET.addCustomEventListener(listener);
	}
	
	public void removeAddedListener(CustomEventListener listener){
		this.addedET.removeCustomEventListener(listener);
	}
	
	public void addChangedListener(CustomEventListener listener){
		this.changedET.addCustomEventListener(listener);
	}
	
	public void removeChangedListener(CustomEventListener listener){
		this.changedET.removeCustomEventListener(listener);
	}
	
	public void addRemovedListener(CustomEventListener listener){
		this.removedET.addCustomEventListener(listener);
	}
	
	public void removeRemovedListener(CustomEventListener listener){
		this.removedET.removeCustomEventListener(listener);
	}

	@Override
	public String toString() {
		return "DataModel [data=" + data + ", sortdata=" + sortdata
				+ ", styles=" + styles + ", sortable=" + sortable
				+ ", colSpan=" + colSpan + ", rowSpan=" + rowSpan + "]";
	}
	
	

}
