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
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

public abstract class DialogDwidget extends DialogBox {

	private SubPanelsDwidget dw;
	public DialogDwidget(String tmpl){
		final DialogDwidget me = this;
		dw = new SubPanelsDwidget(tmpl){
			@Override
			protected void beforeSubPanelsLoading() {
				me.beforeSubPanelsLoading();
			}
			
			@Override
			protected Widget genSubWidget(String dwname, ArrayList<DBObject> params){
				return me.genSubWidget(dwname, params);
			}
		};
		setWidget(dw);
		setText("...");
		setAnimationEnabled(true);
	    setGlassEnabled(true);
	    center();
	}
	
	@Override
	public void setTitle(String title) {
		setText(title);
	}

	protected abstract void beforeSubPanelsLoading();
	protected abstract Widget genSubWidget(String dwname, ArrayList<DBObject> params);
	
}
