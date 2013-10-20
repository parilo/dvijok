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
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.Focusable;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;
import org.dvijok.widgets.auth.socauth.VkAuth;
import org.dvijok.widgets.profile.ProfileSmall;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class HiddenAuth extends SubPanelsDwidget {
	
	private SimplePanel widget;
	private AuthCancelable auth;
	private Anchor switcher;
	private Label empty;
//	private ProfileSmall profilesmall;
	
	public HiddenAuth(SubPanel p){
		super("tmpl/widgets/auth/hiddenauth/hiddenauth.html", p);
	}

//	@Override
//	public boolean needAuthReinit() {
//		return true;
//	}
//
//	@Override
//	public void reinit() {
//		super.reinit();
//		checkGuest();
//	}
	
	private void checkGuest(){
		if( Resources.getInstance().isAuthorized() ){
//			widget.setWidget(profilesmall);
			widget.setWidget(empty);
		} else widget.setWidget(switcher);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		widget = new SimplePanel();
		
		switcher = new Anchor();
		switcher.setHTML("&pi;");
		switcher.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				widget.setWidget(auth);
				if( auth instanceof Focusable ) ((Focusable)auth).setFocus(true);
			}});
		
//		auth = new AuthLogPass();
		auth = new VkAuth();
		auth.addCancelListener(new CustomEventListener(){

			@Override
			public void customEventOccurred(CustomEvent evt) {
				widget.setWidget(switcher);
			}});
		
//		profilesmall = new ProfileSmall();
//		Resources.getInstance().dwidgets.addOnAuthReload(profilesmall);
		empty = new Label();
		
		checkGuest();
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		return widget;
	}

}
