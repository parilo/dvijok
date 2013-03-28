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

import java.util.Date;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Random;
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
	
	public static String getUrl(){
		return Document.get().getURL();
	}
	
	public static String getUrlParameter(String paramName){
		return com.google.gwt.user.client.Window.Location.getParameter(paramName);
	}
	
	public static String getUrlHashParameter(String name){
		String token = History.getToken();
		int beg = token.indexOf(name+"=");
		if( beg == -1 ) return null;
		else {
			beg += name.length()+1;
			int end = token.indexOf("&", beg);
			if( end == -1 ) return token.substring(beg);
			else return token.substring(beg, end);
		}
	}
	
	public static String getUrlWithoutParameters(){
		String path = com.google.gwt.user.client.Window.Location.getPath();
		String host = com.google.gwt.user.client.Window.Location.getHost();
		String protocol = com.google.gwt.user.client.Window.Location.getProtocol();
//		String hash = com.google.gwt.user.client.Window.Location.getHash();
		String url = protocol + "//" + host + path /*+ "?gwt.codesvr=127.0.0.1:9997"*/;
		return url;
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
	
	public static String getTitle(){
		return Window.getTitle();
	}
	
	public static void setTitle(String title){
		Window.setTitle(title);
	}
	
	// get random hash: md5( current_timestamp + ' ' + random_number )
	public static String getRandHash(){
		Date d = new Date();
		return md5.md5(d.getTime()+""+Random.nextInt());
	}
	
	public static String insertPostfix(String url, String postfix){
		int dotpos = url.lastIndexOf(".");
		return url.substring(0, dotpos)+postfix+url.substring(dotpos);
	}
	
}
