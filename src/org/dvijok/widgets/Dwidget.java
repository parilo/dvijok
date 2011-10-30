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
import org.dvijok.handlers.DVRequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class Dwidget extends Composite {
	
	private String tmplUrl;
	private ArrayList<DBObject> params;
	private HashMap<String, HTMLPanel> modes;
	private String dbid;
	private String dwid;
	
	private SimplePanel maincont;
	private HTMLPanel main;
	
	private SubPanel panel;
	
	public Dwidget(String templUrl) {
		this.modes = new HashMap<String, HTMLPanel>();
		this.beforeTmplInit();
		this.init(templUrl);
	}
	
	public Dwidget(String templUrl, SubPanel p) {
		this.modes = new HashMap<String, HTMLPanel>();
		this.panel = p;
		this.readParams();
		this.readDbid();
		this.readDwid();
		this.panel.clear();
		this.panel.getElement().setInnerHTML("");
		this.beforeTmplInit();
		this.init(templUrl);
	}
	
	protected void beforeTmplInit(){}
	public void reinit(){}
	
	private void init(final String templUrl){
		
		this.maincont = new SimplePanel();
		this.initWidget(this.maincont);
		this.tmplUrl = templUrl;
		this.getTmpl();
	}
	
	private void getTmpl(){
		
		Resources.getInstance().tmpls.getTemplate(this.tmplUrl, new DVRequestHandler<String>(){
			@Override
			public void success(String result) {
				tmplUrl = result;
				createGUI();
			}

			@Override
			public void fail(String message) {
				Lib.alert("cannot get template "+tmplUrl+" : "+message);
			}
		});
		
	}
	
	private void readParams(){
		this.params = Resources.getInstance().loader.getParams(this.panel);
	}
	
	public ArrayList<DBObject> getParams(){
		return this.params;
	}
	
	private void readDbid(){
		this.dbid = Resources.getInstance().loader.getAttribute(this.panel, "dbid");
	}
	
	private void readDwid(){
		this.dwid = Resources.getInstance().loader.getAttribute(this.panel, "dwid");
	}
	
	public String getDbid(){
		return this.dbid;
	}
	
	public String getDwid(){
		return this.dwid;
	}
	
	public HTMLPanel getHTMLPanel(){
		return this.main;
	}
	
	protected void createGUI(){
		this.initTmpl();
		this.attachTmpl();
	}
	
	protected void initTmpl(){
		this.main = new HTMLPanel(tmplUrl);
		this.modes.put(this.tmplUrl, this.main);
	}
	
	protected void attachTmpl(){
		this.maincont.setWidget(this.main);
		Resources.getInstance().loader.loadNew();
	}
	
	protected void changeTmpl(String url){
		this.tmplUrl = url;
		if( this.modes.containsKey(url) ){
			this.main = this.modes.get(url);
			this.maincont.setWidget(this.main);
		} else this.getTmpl();
	}

}
