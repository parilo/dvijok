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

package org.dvijok.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import com.fredhat.gwt.xmlrpc.client.XmlRpcClient;
import com.fredhat.gwt.xmlrpc.client.XmlRpcRequest;
import org.dvijok.lib.Lib;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class DataBase {
	
	private XmlRpcClient clientaa;
	
	public DataBase(String xmlrpcurl) {

		clientaa = new XmlRpcClient(xmlrpcurl);
		Lib.Alert("client: "+this.clientaa);

	}
	
	private String Extract(Object o){
		String s = (String)o;
		return s.toUpperCase().equals("NULL")?"":s;
	}
	
	private Serializable convert_type(Object o){
		String classname = o.getClass().getName();
//		System.out.println("  "+classname);
		
		if(classname.equals("java.util.HashMap")){
			return this.convert(o);
		} else if( classname.equals("[Ljava.lang.Object;")) {
			Object[] array = (Object[])o;
//			Serializable[] sa = new Serializable[array.length];
			DB_Object sa = new DB_Object();
			
			for(int ii=0; ii<array.length; ii++){
//				sa[ii] = this.convert_type(array[ii]);
				sa.put(Integer.toString(ii), this.convert_type(array[ii]));
			}
			return sa;
		} else if( classname.equals("java.lang.String")){
//			System.out.println("  "+(String)o);
			return (String)o;
		}
		
		return null;
	}
	
	private DB_Object convert(Object o){
		DB_Object dbo = new DB_Object();
		
    	HashMap<String,Object> obs = (HashMap<String,Object>)o;
    	
    	Iterator<String> i = obs.keySet().iterator();
    	while( i.hasNext() ){
    		String k = i.next();
//    		System.out.println(k);
    		dbo.put(k, this.convert_type(obs.get(k)));
    	}
		
		return dbo;
	}
	
	public void Get_DB_Objects(String source, DB_Object params) {
		
//		System.out.println("source: "+source);
		
//	    Object[] xmlrpcparams = new Object[]{params};
//		DB_Object ret = new DB_Object();
		
		String methodName = "hello";
		Object[] params1 = new Object[]{3, 4};
		 
		XmlRpcRequest<String> request = new XmlRpcRequest<String>(
		                       clientaa, methodName, params1, new AsyncCallback<String>() {
		               public void onSuccess(String response) {
		                       // Handle integer response logic here
		            	   Lib.Alert("success: "+response);
		               }

		               public void onFailure(Throwable response) {
		                       String failedMsg = response.getMessage();
		                       // Put other failed response handling logic here
			            	   Lib.Alert("fail: "+failedMsg);
		               }
		});
		request.execute();
		
		
		//return ret;
	}
	
}
