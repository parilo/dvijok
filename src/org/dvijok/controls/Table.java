//    saasw - internet store for client's websites of saas project written in gwt
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

package org.dvijok.controls;

import java.util.ArrayList;
import java.util.HashMap;

import org.dvijok.lib.Lib;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Table extends Composite {
	
	private FlexTable table;
	private int colnum;
	private int rownum;
	private int rowi;
	private int footer_i;
	private boolean hasfooter;
	private ArrayList<Integer> rowSpanSkip;
	private int rowEnd;
	
	public Table(){
		
		this.table = new FlexTable();
		this.colnum = 0;
		this.rownum = 0;
		this.rowi = 0;
		this.footer_i = 0;
		this.hasfooter = false;
		
		rowSpanSkip = new ArrayList<Integer>();
		rowEnd = -1;
		
		this.table.addStyleName("dwtable");
		
		this.initWidget(this.table);
	}
	
	public HandlerRegistration addClickHandler(ClickHandler handler){
		return this.table.addClickHandler(handler);
	}
	
	public Cell getCellForEvent(ClickEvent event){
		return this.table.getCellForEvent(event);
	}
	
	public void Insert_Cell(int row, int column, String s){
		this.Insert_Cell(row, column, new Label(s));
	}
	
	public void Insert_Cell(int row, int column, Widget w){
		this.table.setWidget(row, column, w);
		this.table.getCellFormatter().addStyleName(row, column, "td");
	}
	
	public void Remove_Widget(Widget w){
		this.table.remove(w);
	}
	
	public void Remove_Cell(int row, int column){
		this.table.removeCell(row, column);
	}
	
	public void Remove_Row(int row){
		this.table.removeRow(row);
		this.rownum--;
	}

	public Widget Get_Widget(int row, int column){
		return this.table.getWidget(row, column);
	}
	
	public int Find_Widget_In_Row(Widget w, int row){
		for( int i=0; i<this.table.getCellCount(row); i++ ){
			if( this.table.getWidget(row, i).equals(w) ) return i;
		}
		return -1;
	}
	
	private void Row_Inc(){
		if( this.rowi == rowEnd  ){
			this.rownum++;
			this.rowi = 0;
			this.table.insertRow(this.rownum);
			this.table.getRowFormatter().addStyleName(this.rownum, "tr");
			if( this.rowSpanSkip.size() > 0 ) this.rowEnd = this.colnum - 1 - this.rowSpanSkip.remove(0);
			else this.rowEnd = this.colnum - 1;
		} else {
			this.rowi++;
		}
	}
	
	private void Add_Row_Span_Skip(int span){
		int inc = span - this.rowSpanSkip.size();
		int add;
		if( inc < 0 ) add = -inc;
		else add = this.rowSpanSkip.size();
			
		for(int i=0; i<add; i++){
			this.rowSpanSkip.set(i, this.rowSpanSkip.get(i)+1);
		}
		
		for(int i=0; i<inc; i++){
			this.rowSpanSkip.add(1);
		}
	}
	
	public void Set_Column_Number(int n){
		this.colnum = n;
		this.rowi = -1;
		this.rowEnd = n-1;
	}
	
	public void Add_Header_Col(String s){
		this.Add_Header_Col(new Label(s));
	}
	
	public void Add_Header_Col(Widget w){
		this.rowi = this.colnum;
		this.table.setWidget(0, this.colnum, w);
		this.table.getCellFormatter().addStyleName(0, this.colnum++, "th");
		this.rowEnd++;
	}
	
	public void Add_Cell(String s){
		this.Add_Cell(new Label(s));
	}
	
	public void Add_Cell(String s, int colspan, int rowspan){
		this.Add_Cell(new Label(s), colspan, rowspan);
	}
	
	public void Add_Cell(Widget w){
		if( this.colnum > 0 ){
			this.Row_Inc();
			this.table.setWidget(this.rownum, this.rowi, w);
			this.table.getCellFormatter().addStyleName(this.rownum, this.rowi, "td");
		}
	}
	
	public void Add_Cell(Widget w, int colspan, int rowspan){
		if( this.colnum > 0 ){
			this.Row_Inc();
			this.Add_Row_Span_Skip(rowspan-1);
			this.table.setWidget(this.rownum, this.rowi, w);
			this.table.getCellFormatter().addStyleName(this.rownum, this.rowi, "td");
			this.table.getFlexCellFormatter().setColSpan(this.rownum, this.rowi, colspan);
			this.table.getFlexCellFormatter().setRowSpan(this.rownum, this.rowi, rowspan);
		}
	}
	
	public Widget Remove_Widget_From_Cell(int row, int col){
		Widget w = this.table.getWidget(row, col);
		this.table.remove(w);
		return w;
	}
	
	public void Add_Widget_To_Cell(Widget w, int row, int col){
		this.table.setWidget(row, col, w);
	}
	
	public void Add_Cell_Style(String style, int row, int col){
		this.table.getCellFormatter().addStyleName(row, col, style);
	}
	
	public void Remove_Cell_Style(String style, int row, int col){
		this.table.getCellFormatter().removeStyleName(row, col, style);
	}
	
	public void Add_Cell_Style(String style){
		this.table.getCellFormatter().addStyleName(this.rownum, this.rowi, style);
	}
	
	public void Add_Footer_Cell(Widget w){
		this.hasfooter = true;
		this.table.setWidget(1, this.footer_i++, w);
	}
	
	public void Clear(){
		int dec = this.hasfooter?2:1;
		
		for(int i=(this.table.getRowCount()-dec); i>0; i--) this.table.removeRow(i);
		this.rownum = 0;
		this.rowi = this.colnum-1;
	}
	
	public void Clear_Full(){
		this.table.removeAllRows();
		this.colnum = 0;
		this.rownum = 0;
		this.rowi = 0;
	}

}
