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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class SelectTime extends Composite {

	private Select hours;
	private Select minuts;
	
	public SelectTime(){
		
		hours = new Select();
		minuts = new Select();

		for(int i=0; i<24; i++){
			String ii = Integer.toString(i);
			hours.addItem(ii, ii);
		}
		
		for(int i=0; i<60; i++){
			String ii = Integer.toString(i);
			minuts.addItem(ii, ii);
		}
		
		hours.addStyleName("hour");
		minuts.addStyleName("min");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(hours);
		hp.add(new Label(":"));
		hp.add(minuts);
		
		initWidget(hp);
		
	}
	
	public String getTime(){
		return hours.getSelectedString()+":"+minuts.getSelectedString();
	}
	
	public void setTime(String hour, String minute){
		hours.setSelectedVal(hour);
		minuts.setSelectedVal(minute);
	}
	
}
