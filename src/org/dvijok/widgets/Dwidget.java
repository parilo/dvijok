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
import java.util.HashMap;

import org.dvijok.db.DBObject;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class Dwidget extends Composite {
	
	private String tmpl_url;
	private ArrayList<DBObject> params;
	private HashMap<String, HTMLPanel> modes;
	private String dbid;
	private String dwid;
	
	private SimplePanel maincont;
	private HTMLPanel main;
	
	private Sub_Panel panel;
	
	public Dwidget(String templ_url) {
		this.modes = new HashMap<String, HTMLPanel>();
		this.Before_Tmpl_Init();
		this.Init(templ_url);
	}
	
	public Dwidget(String templ_url, Sub_Panel p) {
		this.modes = new HashMap<String, HTMLPanel>();
		this.panel = p;
		this.Read_Params();
		this.Read_dbid();
		this.Read_dwid();
		this.panel.clear();
		this.panel.getElement().setInnerHTML("");
		this.Before_Tmpl_Init();
		this.Init(templ_url);
	}
	
	protected void Before_Tmpl_Init(){}
	public void Reinit(){}
	
	private void Init(final String templ_url){
		
		this.maincont = new SimplePanel();
		this.initWidget(this.maincont);
		this.tmpl_url = templ_url;
		this.Get_Tmpl();
	}
	
	private void Get_Tmpl(){
		
		Resources.getInstance().tmpls.Get_Template(this.tmpl_url, new DV_Request_Handler<String>(){
			@Override
			public void Success(String result) {
				tmpl_url = result;
				Create_GUI();
			}

			@Override
			public void Fail(String message) {
				Lib.Alert("cannot get template "+tmpl_url+" : "+message);
			}
		});
		
	}
	
	private void Read_Params(){
		this.params = Resources.getInstance().loader.Get_Params(this.panel);
	}
	
	public ArrayList<DBObject> Get_Params(){
		return this.params;
	}
	
	private void Read_dbid(){
		this.dbid = Resources.getInstance().loader.Get_Attribute(this.panel, "dbid");
	}
	
	private void Read_dwid(){
		this.dwid = Resources.getInstance().loader.Get_Attribute(this.panel, "dwid");
	}
	
	public String Get_dbid(){
		return this.dbid;
	}
	
	public String Get_dwid(){
		return this.dwid;
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
		this.modes.put(this.tmpl_url, this.main);
	}
	
	protected void Attach_Tmpl(){
		this.maincont.setWidget(this.main);
		Resources.getInstance().loader.Load_New();
	}
	
	protected void Change_Tmpl(String url){
		this.tmpl_url = url;
		if( this.modes.containsKey(url) ){
			this.main = this.modes.get(url);
			this.maincont.setWidget(this.main);
		} else this.Get_Tmpl();
	}

}
