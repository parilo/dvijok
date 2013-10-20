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

package org.dvijok.widgets.toolbar.top;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.lib.md5;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;
import org.dvijok.widgets.fx.gfx.VerticalCollapse;
import org.dvijok.widgets.fx.gfx.VerticalExpansion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TopToolbar extends SubPanelsDwidget {

	private InlineHTML username;
	private Anchor showfav;
	private InlineHTML favcount;
	private Anchor logout;
	
	public TopToolbar(){
		super("tmpl/widgets/toolbar/top/toptoolbarguest.html");
		pickMode();
	}
	
	public TopToolbar(SubPanel p){
		super("tmpl/widgets/toolbar/top/toptoolbarguest.html", p);
		pickMode();
	}
	
	private void pickMode(){
		if( Resources.getInstance().isAuthorized() ){
			
			update();
			
			changeTmpl("tmpl/widgets/toolbar/top/toptoolbaranim.html");
			VerticalExpansion a = new VerticalExpansion();
			addStyleName("tmp");
			addStyleName("topmenu");
			a.addEndListener(new CustomEventListener(){
				@Override
				public void customEventOccurred(CustomEvent evt) {
					changeTmpl("tmpl/widgets/toolbar/top/toptoolbarauthed.html");
				}});
			performAnimation(a);
			
		} else if( !username.getText().equals("") ) {

			changeTmpl("tmpl/widgets/toolbar/top/toptoolbaranim.html");
//			changeTmpl("tmpl/widgets/toolbar/top/toptoolbarguest.html");
			VerticalCollapse a = new VerticalCollapse();
			a.addEndListener(new CustomEventListener(){
				@Override
				public void customEventOccurred(CustomEvent evt) {
					username.setText("");
					changeTmpl("tmpl/widgets/toolbar/top/toptoolbarguest.html");
//					removeStyleName("topmenu");
				}});
			performAnimation(a);
			
		}
	}
	
	private void update(){
		username.setText(Resources.getInstance().userInfo.getString("name"));
	}

//	@Override
//	public boolean needAuthReinit() {
//		return true;
//	}
//
//	@Override
//	public void reinit() {
//		super.reinit();
//		pickMode();
//	}

	@Override
	protected void initInternals() {
		username = new InlineHTML("");
		
		//<a href="#" class="secondary tiny button radius3 fav">Избранное</a>
		showfav = new Anchor("Показать избранное");//красиво промотрать вниз и показать только избранное
		showfav.addStyleName("tiny");
		showfav.addStyleName("secondary");
		showfav.addStyleName("button");
		showfav.addStyleName("radius3");
		showfav.addStyleName("fav");
		showfav.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Lib.alert("show favorites");
			}
		});
		
		favcount = new InlineHTML("0");
		
		//<a href="#" class="tiny secondary button radius3 exit">
		logout = new Anchor("Выход");
		logout.addStyleName("tiny");
		logout.addStyleName("secondary");
		logout.addStyleName("button");
		logout.addStyleName("radius3");
		logout.addStyleName("exit");
		logout.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Resources.getInstance().authTool.logout();
			}
		});
		
//		setStartAnimation(new VerticalExpansion());
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("username") ) return username;
		else if( dwname.equals("logout") ) return logout;
		else if( dwname.equals("showfav") ) return showfav;
		else if( dwname.equals("favcount") ) return favcount;
		else return null;
	}

	
}
