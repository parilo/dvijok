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

package org.dvijok.widgets.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.dvijok.db.DBObject;
import org.dvijok.interfaces.DV_Request_Handler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MoreVerticalPanel extends Sub_Panels_Dwidget {

	private VerticalPanel content;
	private Button more;
	
	private String dbid;
	private int begi;
	private int count;
	private WidgetCreator creator;
	
	public MoreVerticalPanel(String dbid, int begIndex, int count, WidgetCreator creator){
		super("tmpl/widgets/container/moreverticalpanel.html");
		this.dbid = dbid;
		this.begi = begIndex;
		this.count = count;
		this.creator = creator;
		load();
	}
	
	public void remove(Widget w){
		content.remove(w);
		begi--;
	}
	
	private void load(){
		
		Resources.getInstance().setBusy(true);
		
		DBObject reqp = new DBObject();
		reqp.put("dbid", dbid);
		reqp.put("beg", Integer.toString(begi));
		reqp.put("count", Integer.toString(count));
		
		Resources.getInstance().db.Get_Objects(reqp, new DV_Request_Handler<DBObject>(){
	
			@Override
			public void Success(DBObject result) {
				load(result.Get_DB_Object("objects"));
				Resources.getInstance().setBusy(false);
			}
	
			@Override
			public void Fail(DBObject result) {
				Lib.Alert("Messages: Load_Messages: failed: "+result);
				Resources.getInstance().setBusy(false);
			}
			
		});
	
	}
	
	private void load(DBObject objs){
		
		ArrayList<DBObject> os = new ArrayList<DBObject>();
		long i=1;
		while( objs.containsKey(Long.toString(i)) ){
			String ii = Long.toString(i++);
			DBObject obj = objs.Get_DB_Object(ii);
			os.add(obj);
		}
		begi += os.size();
		Collections.sort(os, new Comparator<DBObject>(){
			@Override
			public int compare(DBObject o1, DBObject o2) {
				long id1 = o1.Get_Long("id");
				long id2 = o2.Get_Long("id");
				return id2>=id1?id2==id1?0:1:-1;
			}});
		
		for(int oi=0; oi<os.size(); oi++){
			content.add(creator.getWidget(os.get(oi)));
		}
		
	}
	
	public Widget add(DBObject obj){
		Widget w = creator.getWidget(obj);
		if( content.getWidgetCount() > 0 ) content.insert(w, 0);
		else content.add(w);
		return w;
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		content = new VerticalPanel();
		content.addStyleName("dw-moreverticalpanel");
		more = new Button("Показать еще");
		more.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				load();
			}
		});
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("content") ){
			return content;
		} else if( dwname.equals("more") ){
			return more;
		} else return null;
	}
	
}
