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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class UL extends ComplexPanel
{
	public UL()
	{
		setElement(Document.get().createULElement());
	}

	public void setId(String id)
	{
		// Set an attribute common to all tags
		getElement().setId(id);
	}

	public void setDir(String dir)
	{
		// Set an attribute specific to this tag
		((UListElement) getElement().cast()).setDir(dir);
	}

	public void add(Widget w)
	{
		// ComplexPanel requires the two-arg add() method
		super.add(w, getElement());
	}
	
//	public void clear(){
//		clear();
//	}
}
