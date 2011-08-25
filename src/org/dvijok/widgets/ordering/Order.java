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

package org.dvijok.widgets.ordering;

import java.util.ArrayList;
import java.util.List;

import org.dvijok.controls.Table;
import org.dvijok.db.DB_Object;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Order extends Sub_Panels_Dwidget {
	
	private Table objects;
	private Image up;
	private Image down;
	private int selind = -1;
	private ArrayList<Object> vals;

	public Order(){
		super("tmpl/widgets/ordering/order/order.html");
	}

	public void addItem(String s, Object value){
		this.addItem(new Label(s), value);
	}
	
	public void addItem(Widget w, Object value){
		w.addStyleName("dworderobj");
		this.objects.Add_Cell(w);
		this.vals.add(value);
	}
	
	public List<Object> getVals(){
		return vals;
	}
	
	public void removeItem(Object value){
		int ind = this.vals.indexOf(value);
		this.vals.remove(ind);
		this.objects.Remove_Row(ind);
		if( selind > ind ) selind--;
		else if( selind == ind ) selind = -1;
	}
	
	public int getCount(){
		return this.vals.size();
	}
	
	public void clear(){
		vals.clear();
		this.objects.Clear_Full();
		this.objects.Set_Column_Number(1);
		this.selind = -1;
	}
	
	@Override
	protected void Before_Sub_Panels_Loading() {
		vals = new ArrayList<Object>();
		
		this.objects = new Table();
		this.objects.Set_Column_Number(1);
		this.objects.setStylePrimaryName("dworderobjs");
		
		this.objects.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if( selind != -1 ) objects.Remove_Cell_Style("sel", selind, 0);
				Cell c = objects.getCellForEvent(event);
				selind = c.getRowIndex();
				objects.Add_Cell_Style("sel", selind, 0);
			}
		});
		
		this.up = new Image("tmpl/widgets/ordering/order/up.png");
		this.up.addStyleName("dworderup");
		this.up.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if( selind > 0 ){
					Widget w = objects.Remove_Widget_From_Cell(selind, 0);
					objects.Add_Widget_To_Cell(objects.Remove_Widget_From_Cell(selind-1, 0), selind, 0);
					objects.Remove_Cell_Style("sel", selind, 0);
					Object val = vals.get(selind);
					vals.set(selind, vals.get(selind-1));
					selind--;
					objects.Add_Cell_Style("sel", selind, 0);
					objects.Add_Widget_To_Cell(w, selind, 0);
					vals.set(selind, val);
				}
			}
		});
		
		this.down = new Image("tmpl/widgets/ordering/order/down.png");
		this.down.addStyleName("dworderdown");
		this.down.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if( selind < (vals.size()-1) && selind > -1 ){
					Widget w = objects.Remove_Widget_From_Cell(selind, 0);
					objects.Add_Widget_To_Cell(objects.Remove_Widget_From_Cell(selind+1, 0), selind, 0);
					objects.Remove_Cell_Style("sel", selind, 0);
					Object val = vals.get(selind);
					vals.set(selind, vals.get(selind+1));
					selind++;
					objects.Add_Cell_Style("sel", selind, 0);
					objects.Add_Widget_To_Cell(w, selind, 0);
					vals.set(selind, val);
				}
			}
		});
		
	}

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		if( dwname.equals("objects") ) {
			return this.objects;
		} else if( dwname.equals("up") ) {
			return this.up;
		} else if( dwname.equals("down") ) {
			return this.down;
		} else return null;
	}
	
}
