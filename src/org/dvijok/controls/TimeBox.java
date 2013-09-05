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

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;

public class TimeBox extends TextBox {

	private char[] time;
	private NumberFormat format;
	
	public TimeBox(){
		format = NumberFormat.getFormat("00");
		
		this.time = new char[4];
		this.time[0] = '0';
		this.time[1] = '0';
		this.time[2] = '0';
		this.time[3] = '0';
		this.setText("00:00");
		
	    this.addKeyPressHandler(new KeyPressHandler() {
	        public void onKeyPress(KeyPressEvent event) {
	        	char ch = event.getCharCode();
	        	if (Character.isDigit(ch)) {
	        		Typed(ch);
	        	}
        		((TextBox) event.getSource()).cancelKey();
	        }
	    });

	    this.addFocusHandler(new FocusHandler(){
			@Override
			public void onFocus(FocusEvent event) {
				setSelectionRange(0, 1);
			}
	    });
	}
	
	private void Typed(char ch){
		switch(this.getCursorPos()){
			case 0:
			case 5:
				if( ch == '0' || ch == '1' || ch == '2' ){
					this.time[0] = ch;
					this.refresh();
					this.setCursorPos(1);
					this.setSelectionRange(1, 1);
				}
				break;
			case 1:
				if( setHour(ch) ){
					this.refresh();
					this.setCursorPos(3);
					this.setSelectionRange(3, 1);
				}
				break;
			case 2:
			case 3:
				int chval = Integer.parseInt(""+ch);
				if( chval >= 0 && chval < 6 ){
					this.time[2] = ch;
					this.refresh();
					this.setCursorPos(4);
					this.setSelectionRange(4, 1);
				}
				break;
			case 4:
				if( setMinute(ch) ){
					this.refresh();
					this.setCursorPos(0);
					this.setSelectionRange(0, 1);
				}
				break;
		}
		
	}
	
	private boolean setHour(char ch){
		int h = Integer.parseInt(time[0]+""+ch);
		if( h >= 0 && h < 24 ){
			time[1] = ch;
			return true;
		}
		else return false;
	}
	
	private boolean setMinute(char ch){
		int h = Integer.parseInt(time[2]+""+ch);
		if( h >= 0 && h < 60 ){
			time[3] = ch;
			return true;
		}
		else return false;
	}
	
	private void refresh(){
		this.setText(""+time[0]+time[1]+":"+time[2]+time[3]);
	}

	@Override
	public void setText(String text) {
		time[0] = text.charAt(0);
		time[1] = text.charAt(1);
		time[2] = text.charAt(3);
		time[3] = text.charAt(4);
		super.setText(text);
	}
	
	public int getAsSeconds(){
		int ret = getHours()*3600+getMinutes()*60;
		return ret;
	}
	
	public int getHours(){
		return Lib.getInt(""+time[0]+time[1]);
	}
	
	public int getMinutes(){
		return Lib.getInt(""+time[2]+time[3]);
	}
	
	public void setTime(int h, int m){
		setText(format.format(h)+":"+format.format(m));
	}
	
}
