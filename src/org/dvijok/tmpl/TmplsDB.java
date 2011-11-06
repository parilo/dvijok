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

package org.dvijok.tmpl;

import java.util.HashMap;

import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.lib.HttpFunctions;
import org.dvijok.resources.Resources;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class TmplsDB {

	private HashMap<String,String> tmpls;
	
	public TmplsDB(){
		this.tmpls = new HashMap<String,String>();
	}
	
	public void getTemplate(final String url, final DVRequestHandler<String> req){
		
		if( this.tmpls.containsKey(url) ){
			req.success(this.tmpls.get(url));
		} else {
			
//			Resources.getInstance().db.pauseListenForEvents();
			HttpFunctions.doGet(url, new RequestCallback(){
	
				@Override
				public void onError(Request request, Throwable exception) {
					req.fail(exception.getMessage());
				}
	
				@Override
				public void onResponseReceived(Request request, Response response) {
					String text = response.getText();
					tmpls.put(url, text);
					req.success(text);
				}
				
			});
//			Resources.getInstance().db.resumeListenForEvents();

		}
	}

	
	
	
	
	
	
}
