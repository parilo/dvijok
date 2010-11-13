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

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class Sub_Panels_Dwidget extends Dwidget {
	
	public Sub_Panels_Dwidget(String templ_url){
		super(templ_url);
	}
	
	public Sub_Panels_Dwidget(String templ_url, Sub_Panel panel){
		super(templ_url, panel);
	}
	
	@Override
	protected void Create_GUI(){
		this.Init_Tmpl();
		this.Load_Sub_Panels();
		this.Attach_Tmpl();
	}
	
	protected abstract Widget Gen_Sub_Widget(String dwname);
	
	private void Load_Sub_Panels(){
		
		com.google.gwt.user.client.Element w;
		HTMLPanel html = this.Get_HTMLPanel();
		
		while( (w = html.getElementById("dw")) != null ){
			String name = w.getAttribute("dwname");
			Widget sw = this.Gen_Sub_Widget(name);
			if( sw == null ) sw = new Label("Don't know dwname: ->"+name+"<-");
			html.add(sw, "dw");
			w.setAttribute("id", "dw_");
		}
	}
	
}
