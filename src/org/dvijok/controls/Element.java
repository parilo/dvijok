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

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class Element extends com.google.gwt.user.client.Element {

	protected Element(){}
	
	public final void clear(){
		
		NodeList<Node> ns = getChildNodes();
		int count = getChildCount();
		for(int i=0; i<count; i++){
			removeChild(ns.getItem(0));
		}
	}

	
	
	
}
