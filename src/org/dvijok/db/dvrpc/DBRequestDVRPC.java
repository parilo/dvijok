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

import org.dvijok.db.DBObject;
import org.dvijok.db.DBRequest;
import org.dvijok.interfaces.DVRequestHandler;
import org.dvijok.lib.HttpClient;
import org.dvijok.lib.Lib;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class DBRequestDVRPC implements DBRequest {
	
	private HttpClient httpClient;
	
	public DBRequestDVRPC(String url){
		httpClient = new HttpClient(url);
	}
	
	public void request(DBObject data, final DVRequestHandler<DBObject> handler){
		
		httpClient.doPost(data.dvSerialize(), new RequestCallback(){

			@Override
			public void onResponseReceived(Request request, Response response) {
				DBObject obj = new DBObject();
				try {
					obj.dvDeserialize(response.getText());
				} catch (DVDeserializeException e) {
					Lib.Alert("DVRPCTransportPOST: request: "+e.getMessage());
				}
				handler.success(obj);
			}

			@Override
			public void onError(Request request, Throwable exception) {
				DBObject obj = new DBObject();
				obj.put("result", "DBRequestDVRPC: onError: "+exception.getMessage());
				handler.fail(obj);
			}});
		
	}

}
