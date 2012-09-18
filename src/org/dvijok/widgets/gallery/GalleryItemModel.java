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

import org.dvijok.db.DBObject;

public class GalleryItemModel {

	private DBObject dbo;
	
	public GalleryItemModel(){}

	public void setDB0(DBObject dbo){
		this.dbo = dbo;
	}
	
	public DBObject getDB0(){
		return dbo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbo == null) ? 0 : dbo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GalleryItemModel other = (GalleryItemModel) obj;
		if (dbo == null) {
			if (other.dbo != null)
				return false;
		} else if (!dbo.equals(other.dbo))
			return false;
		return true;
	}
	
}

