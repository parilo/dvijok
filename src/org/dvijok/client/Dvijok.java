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

package org.dvijok.client;

import java.util.ArrayList;

import org.dvijok.db.DB_Object;
import org.dvijok.loader.Loader;
import org.dvijok.resources.Resources;
import org.dvijok.tmpl.Tmpls_DB;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Dwidget_Creator;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Test;
import org.dvijok.widgets.content.Content_Hash;
import org.dvijok.widgets.menu.HMenu;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dvijok implements EntryPoint {

	public void onModuleLoad() {

		Loader l = new Loader();

		Resources.getInstance().loader = l;
		Resources.getInstance().tmpls = new Tmpls_DB();
		
		this.Register_Dwidgets();
		
		l.Load();
		
	}
	
	private void Register_Dwidgets(){
		
		Loader l = Resources.getInstance().loader;
		
		l.Get_Dwidget_Factory().Register("hmenu", new Dwidget_Creator(){

			@Override
			public Dwidget Get_Dwidget(Sub_Panel p) {
				return new HMenu(p);
			}
		});
		
		l.Get_Dwidget_Factory().Register("content_hash", new Dwidget_Creator(){

			@Override
			public Dwidget Get_Dwidget(Sub_Panel p) {
				return new Content_Hash(p);
			}
		});
		
		l.Get_Dwidget_Factory().Register("test", new Dwidget_Creator(){

			@Override
			public Dwidget Get_Dwidget(Sub_Panel p) {
				return new Test(p);
			}
		});
		
	}
}
