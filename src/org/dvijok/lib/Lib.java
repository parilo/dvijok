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

package org.dvijok.lib;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;

public class Lib {
	
	public static long getLong(String s){
		try{
			return Long.parseLong(s);
		} catch (NumberFormatException e){
			return 0;
		}
	}

	public static int getInt(String s){
		try{
			return Integer.parseInt(s);
		} catch (NumberFormatException e){
			return 0;
		}
	}

	public static double getDouble(String s){
		try{
			return Double.parseDouble(s);
		} catch (NumberFormatException e){
			return 0;
		}
	}

	public static void setPageTitle(String t){
		com.google.gwt.user.client.Window.setTitle(t);
	}
	
	public static void changeHashToken(String token){
		changeHashToken(token, true);
	}
	
	public static void changeHashToken(String token, boolean issueEvent){
		History.newItem(token, issueEvent);
	}
	
	public static String getHashToken(){
		return History.getToken();
	}
	
	public static void alert(String str){
		com.google.gwt.user.client.Window.alert(str);
	}
	
	public static String getDomain(){
		return Document.get().getDomain();
	}
	
	public static void redirect(String url){
		Window.open(url, "_self", ""); 
	}
	
	public static Image getImageSign(String sign){
		return getImageSign(sign, false);
	}

	public static Image getImageSign(String sign, boolean pointer){
		Image im;
		if( sign.equals("o") ) im = new Image("images/ok.png");
		else if( sign.equals("w") ) im = new Image("images/warning.png");
		else if( sign.equals("e") ) im = new Image("images/error.png");
		else im = new Image();
		
		if( pointer ) im.addStyleName("pointer");
		
		return im;
	}
	
}
