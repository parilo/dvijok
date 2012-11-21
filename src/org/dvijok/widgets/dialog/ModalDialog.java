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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.dvijok.controls.table.DataModel;
import org.dvijok.controls.table.Table;
import org.dvijok.controls.table.TableModel;
import org.dvijok.db.DBArray;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;
import org.dvijok.widgets.content.Hider;
import org.dvijok.widgets.dialog.DialogYesNo;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ModalDialog extends SubPanelsDwidget {

	private boolean visible = false;
	private SimplePanel wpanel;
	private Widget w;
	
	public ModalDialog(){
		super("tmpl/widgets/dialog/modaldialog.html");
	}
	
	public ModalDialog(SubPanel p){
		super("tmpl/widgets/dialog/modaldialog.html", p);
	}
	
	public void setVisible(boolean visible){
		if( this.visible != visible ){
			this.visible = visible;
			if( this.visible ) Resources.getInstance().addToTmp(this);
			else Resources.getInstance().removeFromTmp(this);
		}
	}
	
	public void setWidget(Widget widget){
//		w = widget;
//		redraw();
		wpanel.setWidget(widget);
	}
	
	public void setWidth(int width){
		wpanel.getElement().getStyle().setWidth(width, Unit.PX);
		wpanel.getElement().getStyle().setMarginLeft(-width/2, Unit.PX);
	}

	@Override
	protected void beforeSubPanelsLoading() {
//		w = new Label();
		wpanel = new SimplePanel();
		wpanel.setWidget(new Label());
		wpanel.addStyleName("modalcont");
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return wpanel;
		} else return null;
	}

}
