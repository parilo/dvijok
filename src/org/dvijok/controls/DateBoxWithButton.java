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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateBoxWithButton extends Composite {

	private ToggleButton showPicker;
	private DateBox dateBox;
	
	public DateBoxWithButton(){
		
		showPicker = new ToggleButton("...", "...");
		dateBox = new DateBox();
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(dateBox);
		hp.add(showPicker);
		
		this.showPicker.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if( showPicker.isDown() ) dateBox.showDatePicker();
				else dateBox.hideDatePicker();
			}});
		
		initWidget(hp);
		
	}

	public DateBox getDateBox() {
		return dateBox;
	}
	
}
