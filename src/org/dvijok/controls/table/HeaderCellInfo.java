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

public class HeaderCellInfo {

	private Object obj;
	private int cellType;
	private boolean sort;
	private int sortColumn;

	public HeaderCellInfo(Object obj, int cellType) {
		this.obj = obj;
		this.cellType = cellType;
		this.sort = false;
		this.sortColumn = -1;
	}
	public HeaderCellInfo(Object obj, int cellType, boolean sort, int sortColumn) {
		this.obj = obj;
		this.cellType = cellType;
		this.sort = sort;
		this.sortColumn = sortColumn;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public int getCellType() {
		return cellType;
	}
	public void setCellType(int cellType) {
		this.cellType = cellType;
	}
	public boolean isSort() {
		return sort;
	}
	public void setSort(boolean sort) {
		this.sort = sort;
	}
	public int getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(int sortColumn) {
		this.sortColumn = sortColumn;
	}
	
}
