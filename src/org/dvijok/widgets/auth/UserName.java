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
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class UserName extends SubPanelsDwidget {

	private Label username;
	private Anchor backtoadm;
	private ClickHandler backtoadmClick;
	private HandlerRegistration backtoadmClickHR;
	
	public UserName(SubPanel p){
		super("tmpl/widgets/auth/user_name/user_name.html", p);
		checkIsAdminSession();
	}

	@Override
	public boolean needAuthReinit() {
		return true;
	}

	@Override
	public void reinit() {
		super.reinit();
		String name = Resources.getInstance().userInfo.getString("fullname");
		username.setText(name.equals("guest")?"":name);
		checkIsAdminSession();
	}
	
	private void checkIsAdminSession(){
		if( Resources.getInstance().userInfo.getString("isadmin").equals("1") ){
			backtoadm.setText("Назад в кабинет администратора");
			backtoadmClickHR = backtoadm.addClickHandler(backtoadmClick);
			changeTmpl("tmpl/widgets/auth/user_name/user_name_adm.html");
		} else {
			if( backtoadmClickHR != null ){
				backtoadmClickHR.removeHandler();
				changeTmpl("tmpl/widgets/auth/user_name/user_name.html");
			}
		}
	}

	@Override
	protected void beforeSubPanelsLoading() {
		String name = Resources.getInstance().userInfo.getString("fullname");
		username = new Label(name.equals("guest")?"":name);
		backtoadm = new Anchor();
//		backtoadmClick = new ClickHandler(){
//			@Override
//			public void onClick(ClickEvent event) {
//				Lib.redirect(Resources.getInstance().adminUrl);
//			}};
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("username") ) return this.username; 
		else if( dwname.equals("backtoadm") ) return this.backtoadm; 
		else return null;
	}
	
}
