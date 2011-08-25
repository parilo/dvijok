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
import java.util.Collections;
import java.util.Comparator;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;

import com.google.gwt.i18n.client.DateTimeFormat;

public class TableModel {

	private ArrayList<DataModel> data;
	private DataModel colTypes = null;
	private CustomEventTool addedET;
	private CustomEventTool changedET;
	private CustomEventTool removedET;
	private CustomEventTool inDataModelAddedET;
	private CustomEventTool inDataModelChangedET;
	private CustomEventTool inDataModelRemovedET;
	private DataModel comparators = null;
	private int groupColumn = -1;

	public static int COLTYPE_OTHER = 0;
	public static int COLTYPE_STRING = 1;
	public static int COLTYPE_DOUBLE = 2;
	public static int COLTYPE_WIDGET = 3;
	public static int COLTYPE_DATE = 4;
	public static int COLTYPE_DATE_DAY = 5;
	
	public static int SORT_NONE = 0;
	public static int SORT_ASC = 1;
	public static int SORT_DESC = -1;
	
	public TableModel(){
		this.data = new ArrayList<DataModel>();
		this.addedET = new CustomEventTool();
		this.changedET = new CustomEventTool();
		this.removedET = new CustomEventTool();
		this.inDataModelAddedET = new CustomEventTool();
		this.inDataModelChangedET = new CustomEventTool();
		this.inDataModelRemovedET = new CustomEventTool();
	}
	
	private void initDataModelListeners(final DataModel o){
		
		o.addAddedListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				int[] coord = new int[2];
				coord[0] = data.indexOf(o);
				coord[1] = (Integer) evt.getSource();
				inDataModelAddedET.invokeListeners(coord);
			}
		});
		
		o.addChangedListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				int[] coord = new int[2];
				coord[0] = data.indexOf(o);
				coord[1] = (Integer) evt.getSource();
				inDataModelChangedET.invokeListeners(coord);
			}
		});
		
		o.addRemovedListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				int[] coord = new int[2];
				coord[0] = data.indexOf(o);
				coord[1] = (Integer) evt.getSource();
				inDataModelRemovedET.invokeListeners(coord);
			}
		});
		
	}
	
	public int getSize(){
		return this.data.size();
	}
	
	public DataModel get(int index){
		return this.data.get(index);
	}
	
	public void add(DataModel o){
		int ind = this.data.size();
		this.data.add(o);
		this.initDataModelListeners(o);
		this.addedET.invokeListeners(ind);
	}
	
	public void insert(int index, DataModel o){
		this.data.add(index, o);
	}
	
	public void remove(DataModel o){
		int ind = this.data.indexOf(o);
		this.data.remove(o);
		this.removedET.invokeListeners(ind);
	}
	
	public void set(int index, DataModel o){
		this.data.set(index, o);
		this.initDataModelListeners(o);
		this.changedET.invokeListeners(index);
	}
	
	public DataModel getComparators() {
		return comparators;
	}

	public void setComparators(DataModel comparators) {
		this.comparators = comparators;
	}

	private class DMPos{
		public DataModel dm;
		public int pos;
	};
	
	public void sort(int col, int sortType){
		this.sort_(col, sortType);
		this.checkGroup();
	}

	private void sort_(final int col, final int sortType){
		if( this.colTypes != null ){
			
			final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
			final DateTimeFormat dtfday = DateTimeFormat.getFormat("yyyy-MM-dd");
			
			ArrayList<DMPos> unsort = new ArrayList<DMPos>();
			for( int i=this.data.size()-1; i>-1; i-- ){
				if( !this.data.get(i).isSortable() ){
					DMPos us = new DMPos();
					us.pos = i;
					us.dm = this.data.remove(i);
					unsort.add(us);
				}
			}
			
			Collections.sort(data, new Comparator<DataModel>(){
				@Override
				public int compare(DataModel o1, DataModel o2) {
					int type = colTypes.getInt(col);
					if( type == TableModel.COLTYPE_STRING ){
						return o1.getStringSort(col).compareToIgnoreCase(o2.getStringSort(col))*sortType;
					} else if( type == TableModel.COLTYPE_DOUBLE ){
						return Double.compare(Double.parseDouble(o1.getStringSort(col)), Double.parseDouble(o2.getStringSort(col)))*sortType;
					} else if( type == TableModel.COLTYPE_DATE ){
						return dtf.parse(o1.getStringSort(col)).compareTo(dtf.parse(o2.getStringSort(col)))*sortType;
					} else if( type == TableModel.COLTYPE_DATE_DAY ){
						return dtfday.parse(o1.getStringSort(col)).compareTo(dtfday.parse(o2.getStringSort(col)))*sortType;
					} else if( comparators != null ){
						Comparator compar = (Comparator) comparators.getSort(col);
						if( compar != null ) return compar.compare(o1.getSort(col), o2.getSort(col))*sortType;
						else return 0;
					} else return 0;
				}});

			for(int i=unsort.size()-1; i>-1; i--){
				DMPos us = unsort.get(i);
				this.data.add(us.pos, us.dm);
			}
			
		}
	}
	
	public void checkGroup(){
		if( this.groupColumn != -1 ){
			sort_(this.groupColumn, TableModel.SORT_ASC);
		}
	}
	
	public DataModel getColTypes() {
		return colTypes;
	}

	public void setColTypes(DataModel colTypes) {
		this.colTypes = colTypes;
	}
	
	public int getGroupColumn() {
		return groupColumn;
	}

	public void setGroupColumn(int groupColumn) {
		this.groupColumn = groupColumn;
	}

	public void clear(){
		this.data.clear();
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
	
	public void addInDataModelAddedListener(CustomEventListener listener){
		this.inDataModelAddedET.addCustomEventListener(listener);
	}
	
	public void removeInDataModelAddedListener(CustomEventListener listener){
		this.inDataModelAddedET.removeCustomEventListener(listener);
	}
	
	public void addInDataModelChangedListener(CustomEventListener listener){
		this.inDataModelChangedET.addCustomEventListener(listener);
	}
	
	public void removeInDataModelChangedListener(CustomEventListener listener){
		this.inDataModelChangedET.removeCustomEventListener(listener);
	}
	
	public void addInDataModelRemovedListener(CustomEventListener listener){
		this.inDataModelRemovedET.addCustomEventListener(listener);
	}
	
	public void removeInDataModelRemovedListener(CustomEventListener listener){
		this.inDataModelRemovedET.removeCustomEventListener(listener);
	}
	
}
