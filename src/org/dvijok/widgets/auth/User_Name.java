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
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class User_Name extends Sub_Panels_Dwidget {

	private Label username;
	private Anchor backtoadm;
	private ClickHandler backtoadmClick;
	private HandlerRegistration backtoadmClickHR;
	
	public User_Name(Sub_Panel p){
		super("tmpl/widgets/auth/user_name/user_name.html", p);
		checkIsAdminSession();
	}

	@Override
	public void Reinit() {
		super.Reinit();
		String name = Resources.getInstance().userInfo.Get_String("fullname");
		username.setText(name.equals("guest")?"":name);
		checkIsAdminSession();
	}
	
	private void checkIsAdminSession(){
		if( Resources.getInstance().userInfo.Get_String("isadmin").equals("1") ){
			backtoadm.setText("Назад в кабинет администратора");
			backtoadmClickHR = backtoadm.addClickHandler(backtoadmClick);
			Change_Tmpl("tmpl/widgets/auth/user_name/user_name_adm.html");
		} else {
			if( backtoadmClickHR != null ){
				backtoadmClickHR.removeHandler();
				Change_Tmpl("tmpl/widgets/auth/user_name/user_name.html");
			}
		}
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		String name = Resources.getInstance().userInfo.Get_String("fullname");
		username = new Label(name.equals("guest")?"":name);
		backtoadm = new Anchor();
		backtoadmClick = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Lib.Redirect(Resources.getInstance().adminUrl);
			}};
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		if( dwname.equals("username") ) return this.username; 
		else if( dwname.equals("backtoadm") ) return this.backtoadm; 
		else return null;
	}
	
}
