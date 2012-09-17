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

package org.dvijok.widgets.gallery;

import org.dvijok.db.DBArray;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

public class GalleryModel {

	private DBArray photo;
	
	private CustomEventTool photoChanged;
	
	public GalleryModel(){
		photoChanged = new CustomEventTool();
		photo = new DBArray();
	}
	
	/*
	 * DBArray items must be DBObjects and have field 'src'
	 */
	public void setDBA(DBArray dba){
		photo = dba;
		photoChanged.invokeListeners();
	}
	
	public DBArray getDBA(){
		return photo;
	}

	public void addPhotoChangedListener(CustomEventListener listener){
		photoChanged.addCustomEventListener(listener);
	}
	
	public void removePhotoChangedListener(CustomEventListener listener){
		photoChanged.removeCustomEventListener(listener);
	}
	
}

