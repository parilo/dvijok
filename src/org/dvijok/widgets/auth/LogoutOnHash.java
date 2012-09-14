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

import org.dvijok.db.DBObject;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;

public class LogoutOnHash extends SubPanelsDwidget {

	public LogoutOnHash(SubPanel p){
		super("tmpl/widgets/auth/logoutonhash/logoutonhash.html", p);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String hash = event.getValue();
				if( hash.equals("logout") ){
					
					DBObject dbo = new DBObject();

					final RequestHandler<DBObject> handler = new RequestHandler<DBObject>(){

						@Override
						public void success(DBObject result) {
							//redirect
							Lib.redirect("about:blank");
						}

						@Override
						public void fail(DBObject result) {
							Lib.alert("Logout_On_Hash: Before_Sub_Panels_Loading: logout failed: "+result);
						}
						
					};
					
					Resources.getInstance().db.logout(dbo, handler);
					
				}
			}
		});
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		return null;
	}
	
}
