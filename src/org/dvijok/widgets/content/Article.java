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

package org.dvijok.widgets.content;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class Article extends SubPanelsDwidget {

	private HTML content;
	
	public Article(SubPanel p){
		super("tmpl/widgets/content/article/article.html", p);
		this.initContents();
	}

	@Override
	protected void initInternals() {
		this.content = new HTML();
	}
	
	private void initContents(){
/*		Resources.getInstance().db.Get_DB_Object(this.Get_dbid(),new DV_Request_Handler<DB_Object>(){

			@Override
			public void Success(DB_Object result) {
				content.setHTML(result.Get_String("html"));
			}

			@Override
			public void Fail(String message) {
				Lib.Alert("article loading failed: "+message);
			}
			
		});*/
	}
	
	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return this.content;
		} else return null;
	}
	
}
