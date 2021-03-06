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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class DivPanel extends ComplexPanel {

	private Element div;
	
	public DivPanel(){
		div = DOM.createDiv();
		setElement(div);
	}
	
	@Override
	public void add(Widget child) {
		addWidget(child);
	}

	@Override
	public void add(IsWidget child) {
		addWidget(child.asWidget());
	}

	public void addWidget(Widget w){
		add(w, div);
	}
	
	public void replaceWidget(Widget from, Widget to){
		int i = getWidgetIndex(from);
		insert(to, div, i, true);
		remove(from);
	}
	
}
