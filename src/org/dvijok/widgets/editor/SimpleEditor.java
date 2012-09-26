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

package org.dvijok.widgets.editor;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class SimpleEditor extends SubPanelsDwidget implements Editor {

	private TextArea textarea;
	private Button ok;
	private Button cancel;
	
	private CustomEventTool applyET;
	private CustomEventTool cancelET;
	
	public SimpleEditor(){
		super("tmpl/widgets/editor/simpleeditor.html");
	}
	
	public SimpleEditor(SubPanel p){
		super("tmpl/widgets/estate/simpleeditor.html", p);
	}
	
	public void setFocus(boolean focus){
		textarea.setFocus(focus);
	}
	
	public void addApplyListener(CustomEventListener l){
		applyET.addCustomEventListener(l);		
	}
	
	public void removeApplyListener(CustomEventListener l){
		applyET.removeCustomEventListener(l);		
	}
	
	public void addCancelListener(CustomEventListener l){
		cancelET.addCustomEventListener(l);		
	}
	
	public void removeCancelListener(CustomEventListener l){
	 	cancelET.removeCustomEventListener(l);		
	}

	@Override
	protected void beforeSubPanelsLoading() {
		textarea = new TextArea();
		ok = new Button("Принять");
		cancel = new Button("Отмена");
		
		ok.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				applyET.invokeListeners(getHTML());
			}
		});
		
		cancel.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				cancelET.invokeListeners();
			}
		});
		
		applyET = new CustomEventTool();
		cancelET = new CustomEventTool();
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("textarea") ){
			return textarea;
		} else if( dwname.equals("ok") ){
			return ok;
		} else if( dwname.equals("cancel") ){
			return cancel;
		} else return null;
	}

	@Override
	public String getHTML() {
		return textarea.getValue().replace("\n", "<br/>");
	}

	@Override
	public void setHTML(String html) {
		textarea.setText(html.replace("<br>", "\n"));
	}

	@Override
	public void initEditor() {}

}
