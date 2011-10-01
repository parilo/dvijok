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

package org.dvijok.widgets.dialog;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DialogYesNo extends DialogDwidget {
	
	private Button yes;
	private Button no;
	private Label cont;

	private CustomEventTool  yesET;
	private CustomEventTool  noET;
	
	public DialogYesNo(){
		super("tmpl/widgets/dialog/dialogyesno.html");
		yesET = new CustomEventTool();
		noET = new CustomEventTool();
	}
	
	public void setQuestion(String text){
		cont.setText(text);
	}
	
	public void addYesListener(CustomEventListener listener){
		yesET.addCustomEventListener(listener);
	}
	
	public void addNoListener(CustomEventListener listener){
		noET.addCustomEventListener(listener);
	}

	@Override
	protected void beforeSubPanelsLoading() {
		yes = new Button("Да");
		no = new Button("Нет");
		cont = new Label("");
		
		yes.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				yesET.invokeListeners();
				hide();
			}
		});
		
		no.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				noET.invokeListeners();
				hide();
			}
		});
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return cont;
		} else if( dwname.equals("yes") ){
			return yes;
		} else if( dwname.equals("no") ){
			return no;
		} else return null;
	}

}
