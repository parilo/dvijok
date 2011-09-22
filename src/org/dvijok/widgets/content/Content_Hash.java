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
import java.util.HashMap;

import org.dvijok.db.DBObject;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panel;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Content_Hash extends Sub_Panels_Dwidget {

	private SimplePanel content;
	private HashMap<String,HTMLPanel> contents;
	private HashMap<String,Boolean> loaded;
	private String default_hash;
	
	public Content_Hash(Sub_Panel p){
		super("tmpl/widgets/content/content_hash/content_hash.html", p);
		
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String hash = event.getValue();
				Load_Content(hash);
			}
		});
		
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		this.content = new SimplePanel();
		this.contents = new HashMap<String,HTMLPanel>();
		this.loaded = new HashMap<String,Boolean>();
		this.default_hash = "";
		
		this.Init_Contents();
	}

	@Override
	protected void Create_GUI(){
		super.Create_GUI();
		this.Load_Content(History.getToken());
	}
	
	private void Load_Content(String hash){
		if( contents.containsKey(hash) ){
			this.content.setWidget(contents.get(hash));
			this.Load_Dwidgets(hash);
		} else if( !this.default_hash.equals("") ){
			this.content.setWidget(contents.get(this.default_hash));
			this.Load_Dwidgets(this.default_hash);
		} else {
			this.content.clear();
		}
	}
	
	private void Load_Dwidgets(String hash){
		if(!this.loaded.get(hash)){
			Resources.getInstance().loader.Load_New();
			this.loaded.put(hash, true);
		}
	}
	
	private void Init_Contents(){
		ArrayList<DBObject> ps = this.Get_Params();
		for(int i=0; i<ps.size(); i++){
			DBObject p = ps.get(i);
			String hash = p.Get_String("HASH");
			this.contents.put(hash , new HTMLPanel(p.Get_String("VALUE")));
			this.loaded.put(hash, false);
			if( p.containsKey("DEFAULT") ) this.default_hash = hash;
		}
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return this.content;
		} else return null;
	}
	
}
