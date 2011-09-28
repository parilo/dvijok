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
import org.dvijok.db.gwtrpc.DataBase_GWTRPC;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.interfaces.DVRequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.loader.Dwidgets;
import org.dvijok.loader.Loader;
import org.dvijok.resources.Resources;
import org.dvijok.tmpl.Tmpls_DB;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Dwidget_Creator;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.auth.Auth;
import org.dvijok.widgets.auth.ChangePassword;
import org.dvijok.widgets.auth.Logout_On_Hash;
import org.dvijok.widgets.auth.User_Name;
import org.dvijok.widgets.content.Article;
import org.dvijok.widgets.content.Content_Hash;
import org.dvijok.widgets.content.Content_Hash_DB;
import org.dvijok.widgets.menu.HMenu;
import org.dvijok.widgets.menu.Table_Menu;

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
	Resources.getInstance().tmpls = new Tmpls_DB();
	
	this.Register_Dwidgets();
	
//	Resources.getInstance().Get_User_Info(new DV_Request_Handler<Integer>(){
//
//		@Override
//		public void Success(Integer result) {
			Resources.getInstance().loader.Load();
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

private void Register_Dwidgets(){
	
	Loader l = Resources.getInstance().loader;
	
	l.Get_Dwidget_Factory().Register("hmenu", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new HMenu(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return false;
		}
	});
	
	l.Get_Dwidget_Factory().Register("content_hash", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new Content_Hash(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return false;
		}
	});
	
	l.Get_Dwidget_Factory().Register("content_hash_db", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new Content_Hash_DB(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return true;
		}
	});
	
	l.Get_Dwidget_Factory().Register("article", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new Article(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return false;
		}
	});
	
	l.Get_Dwidget_Factory().Register("auth", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new Auth(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return false;
		}
	});
	
	l.Get_Dwidget_Factory().Register("table_menu", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new Table_Menu(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return false;
		}
	});
	
	l.Get_Dwidget_Factory().Register("logout_on_hash", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new Logout_On_Hash(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return false;
		}
	});
	
	l.Get_Dwidget_Factory().Register("user_name", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new User_Name(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return true;
		}
	});
	
	l.Get_Dwidget_Factory().Register("change_password", new Dwidget_Creator(){

		@Override
		public Dwidget Get_Dwidget(Sub_Panel p) {
			return new ChangePassword(p);
		}

		@Override
		public boolean Need_Auth_Reinit() {
			return true;
		}
	});
	
}
}
