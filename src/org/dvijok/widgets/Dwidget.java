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
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class Dwidget extends Composite {
	
	private String tmpl_url;
	private ArrayList<DB_Object> params;
	private String dbid;
	
	private SimplePanel maincont;
	private HTMLPanel main;
	
	private Sub_Panel panel;
	
	public Dwidget(String templ_url) {
		this.Before_Tmpl_Init();
		this.Init(templ_url);
	}
	
	public Dwidget(String templ_url, Sub_Panel p) {
		this.panel = p;
		this.Read_Params();
		this.Read_dbid();
		this.panel.clear();
		this.panel.getElement().setInnerHTML("");
		this.Before_Tmpl_Init();
		this.Init(templ_url);
	}
	
	protected void Before_Tmpl_Init(){}
	
	private void Init(final String templ_url){
		
		this.maincont = new SimplePanel();
		this.initWidget(this.maincont);
		
		Resources.getInstance().tmpls.Get_Template(templ_url, new DV_Request_Handler<String>(){
			@Override
			public void Success(String result) {
				tmpl_url = result;
				Create_GUI();
			}

			@Override
			public void Fail(String message) {
				Lib.Alert("cannot get template "+templ_url+" : "+message);
			}
		});
		
		this.tmpl_url = templ_url;
	}
	
	private void Read_Params(){
		this.params = Resources.getInstance().loader.Get_Params(this.panel);
	}
	
	public ArrayList<DB_Object> Get_Params(){
		return this.params;
	}
	
	private void Read_dbid(){
		this.dbid = Resources.getInstance().loader.Get_Attribute(this.panel, "dbid");
	}
	
	public String Get_dbid(){
		return this.dbid;
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
		Resources.getInstance().loader.Load(this);
		this.maincont.setWidget(this.main);
	}

}
