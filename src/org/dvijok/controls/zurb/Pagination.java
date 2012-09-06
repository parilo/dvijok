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

package org.dvijok.controls.zurb;

import java.util.HashMap;

import org.dvijok.controls.LI;
import org.dvijok.controls.UL;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public class Pagination extends ComplexPanel {

	private UListElement ul;
	
	private int numberOfAllElements;
	private int displayCount;
	private int currentSelected;
	
/*
	<ul class="pagination">
	  <li class="arrow unavailable"><a href="">&laquo;</a></li>
	  <li class="current"><a href="">1</a></li>
	  <li><a href="">2</a></li>
	  <li><a href="">3</a></li>
	  <li><a href="">4</a></li>
	  <li class="unavailable"><a href="">&hellip;</a></li>
	  <li><a href="">12</a></li>
	  <li><a href="">13</a></li>
	  <li class="arrow"><a href="">&raquo;</a></li>
	</ul>
*/
	
	public Pagination(){
		ul = Document.get().createULElement();
		ul.addClassName("pagination");

		setElement(ul);
	}

	public void setParameters(int numberOfAllElements, int displayCount, int currentSelected){
		this.numberOfAllElements = numberOfAllElements;
		this.displayCount = displayCount;
		this.currentSelected = currentSelected;
		draw();
	}
	
	private void draw(){
		ul.
	}
	
}
