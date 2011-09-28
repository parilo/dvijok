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

import org.dvijok.lib.Lib;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClosablePanel extends Composite {

	private VerticalPanel vp;
	private SimplePanel cont;
	private ToggleButton switcher;
//	private String uptext = "";
//	private String downtext = "";
	
	public ClosablePanel(String uptext, String downtext){
		
		vp = new VerticalPanel();
		
		switcher = new ToggleButton(uptext, downtext);
		switcher.setDown(true);
		
		cont = new SimplePanel();

		FlexTable t = new FlexTable();
		t.setWidget(0, 0, switcher);
		
		vp.add(t);
		vp.add(cont);

		switcher.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				refresh();
			}
		});
		
		initWidget(vp);		
		
	}
	
	private void refresh(){
		if( switcher.isDown() ) {
			if( vp.getWidgetCount() == 1 ) vp.add(cont);
//			switcher.setHTML(downtext);
		} else {
			if( vp.getWidgetCount() == 2 ) vp.remove(cont);
//			switcher.setHTML(uptext);
		}
	}
	
	public void setWidget(Widget w){
		cont.setWidget(w);
	}
	
//	public void setSwitcherText(String uptext, String downtext){
//		this.uptext = uptext;
//		this.downtext = downtext;
//	}
	
//	public void setOpened(boolean opened){
//		this.switcher.setValue(opened);
//		refresh();
//	}

}
