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

import org.dvijok.db.DBObject;
import org.dvijok.resources.Resources;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class SubPanelsDwidget extends Dwidget {
	
	public SubPanelsDwidget(String templUrl){
		super(templUrl);
	}
	
	public SubPanelsDwidget(String templUrl, SubPanel panel){
		super(templUrl, panel);
	}
	
	protected abstract void beforeSubPanelsLoading();
	protected void afterLoading(){}
	
	@Override
	protected void beforeTmplInit(){
		this.beforeSubPanelsLoading();
	}
	
	@Override
	protected void createGUI(){
		this.initTmpl();
		this.loadSubPanels();
		this.attachTmpl();
		afterLoading();
	}
	
	protected abstract Widget genSubWidget(String dwname, ArrayList<DBObject> params);
	
	private void loadSubPanels(){
		
		com.google.gwt.user.client.Element w;
		HTMLPanel html = this.getHTMLPanel();
		
		while( (w = html.getElementById("dw")) != null ){
			String name = w.getAttribute("dwname");
			ArrayList<DBObject> params = Resources.getInstance().loader.getParams(w);
			w.setInnerHTML("");
			Widget sw = this.genSubWidget(name, params);
			if( sw == null ) sw = new Label("Sub_Panels_Dwidget: Don't know dwname: ->"+name+"<-");
			html.add(sw, "dw");
			w.setAttribute("id", "dw_");
		}
	}
	
}
