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

package org.dvijok.widgets.button;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.Dwidget;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ButtonHash extends SubPanelsDwidget {

	private Label button;
	private SubPanel panel;
	private String hash;
	
	public ButtonHash(SubPanel p){
		super("tmpl/widgets/button/buttonhash.html", p);
		p.addStyleName("dwbuttonhash");
		panel = p;
		
		if( History.getToken().equals(hash) ) panel.addStyleName("act");
	}

	@Override
	protected void beforeSubPanelsLoading() {
		ArrayList<DBObject> ps = this.getParams();
		DBObject p = ps.get(0);
		hash = p.getString("HASH");
		String label = p.getString("LABEL");
		
		button = new Label(label);
		
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				Lib.changeHashToken(hash);
			}});
		
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String h = event.getValue();
				if( hash.equals(h) ){
					panel.addStyleName("act");
				} else panel.removeStyleName("act");
			}
		});
		
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		return button;
	}

}
