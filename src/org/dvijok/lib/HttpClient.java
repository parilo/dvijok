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

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

public class HttpClient {
	
	private RequestBuilder builderGET;
	private RequestBuilder builderPOST;
	
	public HttpClient(String url){
		builderGET = new RequestBuilder(RequestBuilder.GET, url);
		builderPOST = new RequestBuilder(RequestBuilder.POST, url);
	}
	
	  public Request doGet(RequestCallback rc) {
	    try {
	      return builderGET.sendRequest(null, rc);
	    } catch (RequestException e) {
	      Lib.alert("Lib: HttpClient: doGet: exception: "+e.getMessage());
	      e.printStackTrace();
	    }
		return null;
	  }

	  public Request doPost(String data, RequestCallback rc) {
	    try {
	      return builderPOST.sendRequest(data, rc);
	    } catch (RequestException e) {
	      Lib.alert("Lib: HttpClient: doPost: exception: "+e.getMessage());
	      e.printStackTrace();
	    }
		return null;
	  }
	  
}
