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

import org.dvijok.db.event.DataBaseEvent;
import org.dvijok.db.event.DataBaseEventListener;
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.lib.Lib;

public class DataBaseTest {

	public DataBaseTest(DataBase db){
		
//putObject test
//		
//		DBObject dbo = new DBObject();
//		dbo.put("dddd", "4444");
//		dbo.put("ffff", "5555");
//		
//		DBObject mdbo = new DBObject();
//		mdbo.put("tags", "tag1 tag2");
//		mdbo.put("dbo", dbo);
//		
//		putObject(mdbo, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				Lib.alert("ret: "+result);
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				// TODO Auto-generated method stub
//				
//			}});

//getObject test
//
//		DBObject dbo = new DBObject();
//		dbo.put("tags", "tag1");
//		dbo.put("offset", "1");
//		
//		getObject(dbo, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				Lib.alert("ret: "+result);
//			}
//
//			@Override
//			public void fail(DBObject result) {}});

//getObjects test
//		
//		DBObject dbo = new DBObject();
//		dbo.put("tags", "tag1");
//		dbo.put("offset", "4");
//		dbo.put("count", "2");
//		
//		getObjects(dbo, new DVRequestHandler<DBArray>(){
//
//			@Override
//			public void success(DBArray result) {
//				Lib.alert("ret: "+result);
//			}
//
//			@Override
//			public void fail(DBArray result) {}});

//getObjects test
//		
//		DBObject dbo = new DBObject();
//		dbo.put("tags", "tag1");
//		dbo.put("count", "2");
//		
//		getObjects(dbo, new DVRequestHandler<DBArray>(){
//
//			@Override
//			public void success(DBArray result) {
//				Lib.alert("ret: "+result);
//				Iterator<Serializable> i = result.iterator();
//				while( i.hasNext() ){
//					DBObject dbo = (DBObject) i.next();
//					
//					DBObject todel = new DBObject();
//					todel.put("id", dbo.getString("id"));
//					delObject(todel, new DVRequestHandler<DBObject>(){
//
//						@Override
//						public void success(DBObject result) {
//							Lib.alert("del: "+result);
//						}
//
//						@Override
//						public void fail(DBObject result) {
//							// TODO Auto-generated method stub
//							
//						}});
//					
//				}
//			}
//
//			@Override
//			public void fail(DBArray result) {}});

//		DBObject todel = new DBObject();
//		todel.put("id", "2");
//		delObject(todel, new DVRequestHandler<DBObject>(){
//
//			@Override
//			public void success(DBObject result) {
//				Lib.alert("del: "+result);
//			}
//
//			@Override
//			public void fail(DBObject result) {
//				// TODO Auto-generated method stub
//				
//			}});
		
//EventListener test
		
		DBObject dbo = new DBObject();
		dbo.put("tags", "tag1");
		db.addEventListener(dbo, new DataBaseEventListener(){

			@Override
			public void objectAdded(DataBaseEvent evt) {
				Lib.alert("t1 add: "+evt.getParams());
			}

			@Override
			public void objectModifyed(DataBaseEvent evt) {
				Lib.alert("t1 mod: "+evt.getParams());
			}

			@Override
			public void objectDeleted(DataBaseEvent evt) {
				Lib.alert("t1 del: "+evt.getParams());
			}

			@Override
			public void allEvent(DataBaseEvent evt) {
				Lib.alert("t1 all-------: "+evt.getParams());
			}});

		
//		dbo = new DBObject();
//		dbo.put("tags", "tag2");
//		db.addEventListener(dbo, new DataBaseEventListener(){
//
//			@Override
//			public void objectAdded(DataBaseEvent evt) {
//				Lib.alert("t2 add: "+evt.getParams());
//			}
//
//			@Override
//			public void objectModifyed(DataBaseEvent evt) {
//				Lib.alert("t2 mod: "+evt.getParams());
//			}
//
//			@Override
//			public void objectDeleted(DataBaseEvent evt) {
//				Lib.alert("t2 del: "+evt.getParams());
//			}
//
//			@Override
//			public void allEvent(DataBaseEvent evt) {
//				Lib.alert("t2 all-------: "+evt.getParams());
//			}});
		
//		dbo = new DBObject();
//		dbo.put("dddd", "4444");
//		dbo.put("ffff", "5555");
//		
//		DBObject mdbo = new DBObject();
//		mdbo.put("tags", "tag1");
//		mdbo.put("dbo", dbo);
//		
//		DVRequestHandler<DBObject> dvrh = new DVRequestHandler<DBObject>(){
//			@Override
//			public void success(DBObject result) {}
//			@Override
//			public void fail(DBObject result) {}};
//		
//		db.putObject(mdbo, dvrh);
//
//		mdbo.put("tags", "tag2");
//		db.putObject(mdbo, dvrh);
//		db.putObject(mdbo, dvrh);
		
		
		
	}
	
}
