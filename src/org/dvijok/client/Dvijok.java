//dvijok - cms written in gwt
//Copyright (C) 2010  Pechenko Anton Vladimirovich aka Parilo
//mailto: forpost78 at gmail dot com
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>
//

package org.dvijok.client;

import java.util.HashMap;
import java.util.Iterator;

import org.dvijok.config.Config;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;
import org.dvijok.loader.Loader;
import org.dvijok.resources.Resources;
import org.dvijok.resources.historywatch.HistoryWatcher;
import org.dvijok.rpc.DBObject;
import org.dvijok.rpc.RPCImpl;
import org.dvijok.tmpl.TmplsDB;
import org.dvijok.widgets.DwidgetCreator;

import com.google.gwt.core.client.EntryPoint;

/**
* Entry point classes define <code>onModuleLoad()</code>.
*/
public class Dvijok implements EntryPoint {

	private HashMap<String, DwidgetCreator> dwidgets;
	
	public Dvijok(HashMap<String, DwidgetCreator> dwidgets){
		this.dwidgets = dwidgets;
	}
	
	public void onModuleLoad() {
	
		Resources.getInstance().initTitle = Lib.getTitle();
		Resources.getInstance().conf = new Config();
		Resources.getInstance().conf.load(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {

				Resources.getInstance().db = new RPCImpl(Resources.getInstance().conf, new CustomEventListener(){
					@Override
					public void customEventOccurred(CustomEvent evt) {
			
						DBObject initData = (DBObject)evt.getSource();
						
						Resources.getInstance().userInfo = initData.getDBObject("userinfo");
						Resources.getInstance().userData = initData.getDBObject("userdata");
			
						Resources.getInstance().historyWatcher = new HistoryWatcher();
			
						Resources.getInstance().loader = new Loader();
						Resources.getInstance().tmpls = new TmplsDB();//this must be init after DataBaseImpl
//						Lib.alert(""+initData.getDBObject("tmplcache"));
						if( initData.containsKey("tmplcache") ) Resources.getInstance().tmpls.addToTemplates(initData.getDBObject("tmplcache"));
						
						registerDwidgets();
						
						Resources.getInstance().loader.load();
			//			Resources.getInstance().init();
						
					}});
				
			}});

	}

	private void registerDwidgets(){
		
		Loader l = Resources.getInstance().loader;
		
	//	l.getDwidgetFactory().register("hmenu", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new HMenu(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("content_hash", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new ContentHash(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("content_hash_db", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new ContentHashDB(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("article", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new Article(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("auth", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new AuthLogPass(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("hiddenauth", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new HiddenAuth(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("vkauth", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new VkAuth(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("authcombo", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new AuthCombo(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("table_menu", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new TableMenu(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("logout_on_hash", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new LogoutOnHash(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("user_name", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new UserName(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("change_password", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new ChangePassword(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("hider", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new Hider(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("profilesmall", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new ProfileSmall(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("buttonhash", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new ButtonHash(p);
	//		}});
	//	
	//	l.getDwidgetFactory().register("toptoolbar", new DwidgetCreator(){
	//
	//		@Override
	//		public Dwidget getDwidget(SubPanel p) {
	//			return new TopToolbar(p);
	//		}});
		
		/*
		 * registering custom dwidgets
		 */
		if( dwidgets != null ){
			Iterator<String> i = dwidgets.keySet().iterator();
			while( i.hasNext() ){
				String name = i.next();
				l.getDwidgetFactory().register(name, dwidgets.get(name));
			}
		}
		
	}
	
	
	
	
	
	
}
