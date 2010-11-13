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

package org.dvijok.widgets;

import java.util.ArrayList;

import org.dvijok.db.DB_Object;
import org.dvijok.interfaces.DV_Request_Callback;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Dwidget extends Composite {
	
	private String tmpl_url;
	private ArrayList<DB_Object> params;
	
	private SimplePanel maincont;
	private HTMLPanel main;
	
	private Sub_Panel panel;
	
	public Dwidget(String templ_url) {
		this.Init(templ_url);
	}
	
	public Dwidget(String templ_url, Sub_Panel p) {
		this.panel = p;
		this.Read_Params();
		this.panel.clear();
		this.panel.getElement().setInnerHTML("");
		this.Init(templ_url);
	}
	
	private void Init(String templ_url){
		Resources.getInstance().tmpls.Get_Template(templ_url, new DV_Request_Callback<String>(){
			@Override
			public void Success(String result) {
				tmpl_url = result;
				Create_GUI();
			}
		});
		
		this.tmpl_url = templ_url;
		this.maincont = new SimplePanel();

		this.initWidget(this.maincont);
	}
	
	private void Read_Params(){
		this.params = Resources.getInstance().loader.Get_Params(this.panel);
	}
	
	public ArrayList<DB_Object> Get_Params(){
		return this.params;
	}
	
	public HTMLPanel Get_HTMLPanel(){
		return this.main;
	}
	
	protected void Create_GUI(){
		this.Init_Tmpl();
		this.Attach_Tmpl();
	}
	
	protected void Init_Tmpl(){
		this.main = new HTMLPanel(tmpl_url);
	}
	
	protected void Attach_Tmpl(){
		this.maincont.setWidget(this.main);
	}

}
