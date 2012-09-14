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

import org.dvijok.db.DBRequest;
import org.dvijok.lib.HttpClient;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;

public class DBRequestDVRPC implements DBRequest {

	private Request request;
	private RequestCallback callback;
	private HttpClient httpClient;
	private String data;
	
	public DBRequestDVRPC(Request request, String data, RequestCallback callback, HttpClient httpClient){
		this.request = request;
		this.callback = callback;
		this.httpClient = httpClient;
		this.data = data;
	}
	
	@Override
	public void cancel() {
		request.cancel();
	}

	@Override
	public void pause(){
		request.cancel();
	}
	
	@Override
	public void resume(){
		request = httpClient.doPost( data, callback);
	}

	@Override
	public boolean isPending() {
		return request.isPending();
	}
	
}
