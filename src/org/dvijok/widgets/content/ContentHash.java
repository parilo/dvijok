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
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentHash extends SubPanelsDwidget {

	private SimplePanel content;
	private HashMap<String,HTMLPanel> contents;
	private HashMap<String,Boolean> loaded;
	private String default_hash;
	
	public ContentHash(SubPanel p){
		super("tmpl/widgets/content/content_hash/content_hash.html", p);
		
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String hash = event.getValue();
				loadContent(hash);
			}
		});
		
	}

	@Override
	protected void beforeSubPanelsLoading() {
		this.content = new SimplePanel();
		this.contents = new HashMap<String,HTMLPanel>();
		this.loaded = new HashMap<String,Boolean>();
		this.default_hash = "";
		
		this.initContents();
	}

	@Override
	protected void createGUI(){
		super.createGUI();
		this.loadContent(History.getToken());
	}
	
	private void loadContent(String hash){
		if( contents.containsKey(hash) ){
			this.content.setWidget(contents.get(hash));
			this.loadDwidgets(hash);
		} else if( !this.default_hash.equals("") ){
			this.content.setWidget(contents.get(this.default_hash));
			this.loadDwidgets(this.default_hash);
		} else {
			this.content.clear();
		}
	}
	
	private void loadDwidgets(String hash){
		if(!this.loaded.get(hash)){
			Resources.getInstance().loader.loadNew();
			this.loaded.put(hash, true);
		}
	}
	
	private void initContents(){
		ArrayList<DBObject> ps = this.getParams();
		for(int i=0; i<ps.size(); i++){
			DBObject p = ps.get(i);
			String hash = p.getString("HASH");
			this.contents.put(hash , new HTMLPanel(p.getString("VALUE")));
			this.loaded.put(hash, false);
			if( p.containsKey("DEFAULT") ) this.default_hash = hash;
		}
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return this.content;
		} else return null;
	}
	
}
