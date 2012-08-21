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
import org.dvijok.db.DataBaseImpl;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.loader.Dwidgets;
import org.dvijok.loader.Loader;
import org.dvijok.resources.Resources;
import org.dvijok.tmpl.TmplsDB;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.DwidgetCreator;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.auth.Auth;
import org.dvijok.widgets.auth.AuthCombo;
import org.dvijok.widgets.auth.AuthLogPass;
import org.dvijok.widgets.auth.ChangePassword;
import org.dvijok.widgets.auth.HiddenAuth;
import org.dvijok.widgets.auth.LogoutOnHash;
import org.dvijok.widgets.auth.UserName;
import org.dvijok.widgets.auth.socauth.VkAuth;
import org.dvijok.widgets.button.ButtonHash;
import org.dvijok.widgets.content.Article;
import org.dvijok.widgets.content.ContentHash;
import org.dvijok.widgets.content.ContentHashDB;
import org.dvijok.widgets.content.Hider;
import org.dvijok.widgets.menu.HMenu;
import org.dvijok.widgets.menu.TableMenu;
import org.dvijok.widgets.profile.ProfileSmall;
import org.dvijok.widgets.toolbar.top.TopToolbar;

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
	Resources.getInstance().db = new DataBaseImpl(new CustomEventListener(){
		@Override
		public void customEventOccurred(CustomEvent evt) {
			
			Resources.getInstance().userInfo = ((DBObject)evt.getSource()).getDBObject("userinfo");
			Resources.getInstance().userData = ((DBObject)evt.getSource()).getDBObject("userdata");

			Resources.getInstance().dwidgets = new Dwidgets();

			Resources.getInstance().loader = new Loader();
			Resources.getInstance().tmpls = new TmplsDB();//this must be init after DataBaseImpl
			
			registerDwidgets();
			
			Resources.getInstance().loader.load();
			Resources.getInstance().init();
			
		}});
	
}

private void registerDwidgets(){
	
	Loader l = Resources.getInstance().loader;
	
	l.getDwidgetFactory().register("hmenu", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new HMenu(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("content_hash", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new ContentHash(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("content_hash_db", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new ContentHashDB(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("article", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new Article(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("auth", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new AuthLogPass(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("hiddenauth", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new HiddenAuth(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("vkauth", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new VkAuth(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("authcombo", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new AuthCombo(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("table_menu", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new TableMenu(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("logout_on_hash", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new LogoutOnHash(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("user_name", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new UserName(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("change_password", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new ChangePassword(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("hider", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new Hider(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("profilesmall", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new ProfileSmall(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
	l.getDwidgetFactory().register("buttonhash", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new ButtonHash(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
		}
	});
	
	l.getDwidgetFactory().register("toptoolbar", new DwidgetCreator(){

		@Override
		public Dwidget getDwidget(SubPanel p) {
			return new TopToolbar(p);
		}

		@Override
		public boolean needAuthReinit() {
			return true;
		}
	});
	
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
