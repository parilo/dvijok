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

package org.dvijok.config;

import java.util.Date;

import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.HttpFunctions;
import org.dvijok.lib.Lib;
import org.dvijok.rpc.DBObject;
import org.dvijok.rpc.RPCConfig;
import org.dvijok.rpc.json.JSONProto;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class Config implements RPCConfig {

	public String rpcUrl;
	public String rpcType;
	public Date sessionExpTime;
	public JSONProto jsonProto;
	
	public Config(){

		this.rpcUrl = "dvrpc/rpc.php";
		this.sessionExpTime = new Date(365*24*60*60*1000); //cookie exp time
		jsonProto = new JSONProto();
		
	}
	
	public void load(final CustomEventListener listener){
		
		HttpFunctions.doGet("clientconfig.json", new RequestCallback(){
			
			@Override
			public void onError(Request request, Throwable exception) {
				Lib.alert("cannot get clientconfig");
			}

			@Override
			public void onResponseReceived(Request request, Response response) {
				String text = response.getText();
				fillData(jsonProto.decode(text));
				listener.customEventOccurred(null);
			}
			
		});
		
	}
	
	private void fillData(DBObject dbo){
		rpcType = dbo.getString("rpcType");
		rpcUrl = dbo.getString("rpcUrl");
		sessionExpTime = new Date(dbo.getLong("sessionExpirationTime"));
	}

	public String getRpcUrl() {
		return rpcUrl;
	}

	public String getRpcType() {
		return rpcType;
	}

	public Date getSessionExpTime() {
		return sessionExpTime;
	}
	
}
