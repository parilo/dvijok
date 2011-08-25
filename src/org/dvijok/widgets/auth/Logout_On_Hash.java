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

package org.dvijok.widgets.auth;

import java.util.ArrayList;

import org.dvijok.db.DB_Object;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.lib.md5;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;

public class Logout_On_Hash extends Sub_Panels_Dwidget {

	public Logout_On_Hash(Sub_Panel p){
		super("tmpl/widgets/auth/logoutonhash/logoutonhash.html", p);
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String hash = event.getValue();
				if( hash.equals("logout") ){
					
					DB_Object dbo = new DB_Object();

					final DV_Request_Handler<DB_Object> handler = new DV_Request_Handler<DB_Object>(){

						@Override
						public void Success(DB_Object result) {
							//redirect
							Lib.Redirect("about:blank");
						}

						@Override
						public void Fail(DB_Object result) {
							Lib.Alert("Logout_On_Hash: Before_Sub_Panels_Loading: logout failed: "+result);
						}
						
					};
					
					Resources.getInstance().db.Logout(dbo, handler);
					
				}
			}
		});
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		return null;
	}
	
}
