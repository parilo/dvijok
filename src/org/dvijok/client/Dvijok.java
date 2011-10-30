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

import org.dvijok.config.Config;
import org.dvijok.db.DataBaseImpl;
import org.dvijok.loader.Dwidgets;
import org.dvijok.loader.Loader;
import org.dvijok.resources.Resources;
import org.dvijok.tmpl.TmplsDB;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.DwidgetCreator;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.auth.Auth;
import org.dvijok.widgets.auth.ChangePassword;
import org.dvijok.widgets.auth.LogoutOnHash;
import org.dvijok.widgets.auth.UserName;
import org.dvijok.widgets.content.Article;
import org.dvijok.widgets.content.ContentHash;
import org.dvijok.widgets.content.ContentHashDB;
import org.dvijok.widgets.menu.HMenu;
import org.dvijok.widgets.menu.TableMenu;

import com.google.gwt.core.client.EntryPoint;

/**
* Entry point classes define <code>onModuleLoad()</code>.
*/
public class Dvijok implements EntryPoint {

public void onModuleLoad() {

	Resources.getInstance().conf = new Config();
	Resources.getInstance().db = new DataBaseImpl();
	Resources.getInstance().dwidgets = new Dwidgets();
	
	Loader l = new Loader();

	Resources.getInstance().loader = l;
	Resources.getInstance().tmpls = new TmplsDB();
	
	this.registerDwidgets();
	
//	Resources.getInstance().Get_User_Info(new DV_Request_Handler<Integer>(){
//
//		@Override
//		public void Success(Integer result) {
			Resources.getInstance().loader.load();
			Resources.getInstance().init();
//			String pagehash = Lib.Get_Hash_Token();
//			if( pagehash.equals("") ){
//				String lasthash = Resources.getInstance().userInfo.Get_String("pagehash");
//				if( lasthash.equals("") ) lasthash="main";
//				Lib.Change_Hash_Token(lasthash);
//			} else {
//				Resources.getInstance().userInfo.put("pagehash", pagehash);
//				Resources.getInstance().Save_User_Info();
//			}
//		}
//
//		@Override
//		public void Fail(Integer result) {}
//	});
	
//	Resources.getInstance().loader.Get_Dwidget_Factory().addDwidgetLoadListener(new CustomEventListener(){
//		@Override
//		public void customEventOccurred(CustomEvent evt) {
//			Dwidget w = (Dwidget)evt.getSource();
//			if( w.Get_dwid().equals("mainmenu") ){
//				Table_Menu tm = (Table_Menu) w;
//				tm.addActionListener(new CustomEventListener(){
//					@Override
//					public void customEventOccurred(CustomEvent evt) {
//						Resources.getInstance().userInfo.put("pagehash", ""+evt.getSource());
//						Resources.getInstance().Save_User_Info();
//					}});
//			}
//		}
//	});

	
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
			return new Auth(p);
		}

		@Override
		public boolean needAuthReinit() {
			return false;
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
	
}
}
