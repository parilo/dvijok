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

package org.dvijok.widgets.links;

import java.util.ArrayList;

import org.dvijok.db.DB_Object;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ImageLink extends Sub_Panels_Dwidget {

	public Image im;
	public Anchor link;
	
	public ImageLink() {
		super("tmpl/widgets/link/imagelink.html");
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		this.im = new Image();
		this.im.addStyleName("pointer");
		this.link = new Anchor("");
	}
	
	public void setImage(String url){
		im.setUrl(url);
	}
	
	public void setText(String text){
		this.link.setText(text);
	}
	
	public void addClickHandler(ClickHandler handler){
		this.im.addClickHandler(handler);
		this.link.addClickHandler(handler);
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		if( dwname.equals("image") ){
			return this.im;
		} else if( dwname.equals("link") ){
			return this.link;
		} else return null;
	}

}
