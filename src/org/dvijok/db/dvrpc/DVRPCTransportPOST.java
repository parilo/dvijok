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

package org.dvijok.db.dvrpc;

import org.dvijok.interfaces.DVRequestHandler;
import org.dvijok.lib.HttpClient;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class DVRPCTransportPOST implements DVRPCTransport {
	
	private HttpClient httpClient;
	
	public DVRPCTransportPOST(String url){
		httpClient = new HttpClient(url);
	}
	
	public void request(String data, DVRequestHandler<String> handler){
		
		httpClient.doPost(data, new RequestCallback(){

			@Override
			public void onResponseReceived(Request request, Response response) {
				handler.success(result);
			}

			@Override
			public void onError(Request request, Throwable exception) {
			}});
		
	}

}
