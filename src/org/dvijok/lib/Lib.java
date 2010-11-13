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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.History;

public class Lib {

	public static int Get_Int(String s){
		try{
			return Integer.parseInt(s);
		} catch (NumberFormatException e){
			return 0;
		}
	}

	public static double Get_Double(String s){
		try{
			return Double.parseDouble(s);
		} catch (NumberFormatException e){
			return 0;
		}
	}

	public static void Set_Page_Title(String t){
		com.google.gwt.user.client.Window.setTitle(t);
	}
	
	public static void Change_Hash_Token(String token){
		Change_Hash_Token(token, true);
	}
	
	public static void Change_Hash_Token(String token, boolean issueEvent){
		History.newItem(token, issueEvent);
	}
	
	public static void Alert(String str){
		com.google.gwt.user.client.Window.alert(str);
	}

}
