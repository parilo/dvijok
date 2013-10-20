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

package org.dvijok.widgets.profile;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProfileSmall extends SubPanelsDwidget {
	
	private Label username;
	private Anchor logout;
	
	public ProfileSmall(){
		super("tmpl/widgets/profile/profilesmall.html");
	}
	
	public ProfileSmall(SubPanel p){
		super("tmpl/widgets/profile/profilesmall.html", p);
	}

//	@Override
//	public boolean needAuthReinit() {
//		return true;
//	}
//
//	@Override
//	public void reinit() {
//		super.reinit();
//		initUsername();
//	}
	
	private void initUsername(){
		
		DBObject dbo = Resources.getInstance().userInfo;
		
		if( Resources.getInstance().isAuthorized() ){
			username.setText(Resources.getInstance().userInfo.getString("username"));
		} else {
			username.setText("unknown");
		}
		
	}

	@Override
	protected void beforeSubPanelsLoading() {
		username = new Label("");
		logout = new Anchor("Выход");
		logout.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				Resources.getInstance().authTool.logout(new RequestHandler<DBObject>(){

					@Override
					public void success(DBObject result) {
					}

					@Override
					public void fail(DBObject result) {
					}});
			}});
		
		initUsername();
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("username") ) return username;
		else if( dwname.equals("logout") ) return logout;
		else return null;
	}

}
