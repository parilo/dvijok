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

package org.dvijok.controls.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.lib.Lib;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Table extends Composite {

	private FlexTable table;
	private DataModel header = null;
	private DataModel headerStyles = null;
	private TableModel headerT = null; // header as TableModel
	private TableModel data;
	private TableModel formattedData;
	private ArrayList<Anchor> sortMarkers;
	private int prevSortCol = -1;
	private int prevSortType = TableModel.SORT_NONE;
	
	private boolean showSummaryTop = false;
	private boolean showSummaryBottom = false;
	private DataModel summators = null;
	private DataModel summaryStyles = null;
	
	private boolean showGroupSummaryTop = false;
	private boolean showGroupSummaryBottom = false;
	private DataModel groupSummators = null;
	private DataModel groupSummaryStyles = null;

	NumberFormat nf = NumberFormat.getFormat("#.##");

	public Table(){
		
		this.sortMarkers = new ArrayList<Anchor>();
		this.data = new TableModel();
		this.formattedData = new TableModel();
		this.table = new FlexTable();
		this.table.addStyleName("dwtable");
		
		this.initModelsListeners();
		
		this.initWidget(this.table);
	}
	
	private void initModelsListeners(){
		this.data.addAddedListener(new CustomEventListener(){

			@Override
			public void customEventOccurred(CustomEvent evt) {
				drawRow((Integer)evt.getSource());
			}
			
		});
	}
	
	private void clearRows(int begind, int countdel, int countadd){
		int delri = begind+countadd;
		for(int i=0; i<countadd; i++) table.insertRow(begind);
//		Lib.Alert("aa: "+begind);
		for(int i=0; i<countdel; i++) table.removeRow(delri);
//		Lib.Alert("dd: "+delri);
	}
	
	private void insertHeader(){
		if( header != null ){
			table.insertRow(0);
		} else if( headerT != null ){
			for(int i=0; i< headerT.getSize(); i++){
				table.insertRow(0);
			}
		}
		drawHeader();
	}

	private void drawHeader(){
		if( header != null ){

			int groupCol = -1;
			if( this.data != null ) groupCol = this.data.getGroupColumn();
			
			DataModel colTypes = this.data.getColTypes();
			
			if( colTypes != null ) {
				
				DataModel compar = this.data.getComparators();
				this.sortMarkers.clear();
				
				int ri=0;
				for(int i=0; i<this.header.getSize(); i++){
					
					if( i != groupCol ){
					
						Widget w;
						int type = colTypes.getInt(i);
						if(
							(type != TableModel.COLTYPE_OTHER && type != TableModel.COLTYPE_WIDGET) ||
							compar != null && compar.get(i) != null
						){
							
							Anchor a = new Anchor(this.header.getString(i));
							
							final int coli = ri;
							
							ClickHandler sortCH = new ClickHandler(){
								@Override
								public void onClick(ClickEvent event) {
									sort(coli);
								}
							};
	
							Anchor sortMarker = new Anchor("");
							this.sortMarkers.add(sortMarker);
							
							a.addClickHandler(sortCH);
							sortMarker.addClickHandler(sortCH);
							
							HorizontalPanel hp = new HorizontalPanel();
							hp.add(a);
							hp.add(new HTML("&nbsp;"));
							hp.add(sortMarker);
							
							w = hp;
						} else {
							if( header.isWidget(i) ){
								w = header.getWidget(i);
							} else w = new Label(this.header.getString(i));
							this.sortMarkers.add(null);
						}

						this.table.setWidget(0, ri, w);
						this.table.getCellFormatter().addStyleName(0, ri, "dwt th");
						if( headerStyles != null ){
							String hstyle = headerStyles.getString(ri);
							if( hstyle != null && !hstyle.equals("") ){
								table.getCellFormatter().addStyleName(0, ri, hstyle);
							}
						}

						ri++;
					}
					
				}
				
			} else
				for(int i=0; i<this.header.getSize(); i++){
					if( this.header.isWidget(i) ) this.table.setWidget(0, i, this.header.getWidget(i));
					else this.table.setWidget(0, i, new Label(this.header.getString(i)));
					this.table.getCellFormatter().addStyleName(0, i, "dwt th");
					if( headerStyles != null ){
						String hstyle = headerStyles.getString(i);
						if( hstyle != null && !hstyle.equals("") ){
							table.getCellFormatter().addStyleName(0, i, hstyle);
						}
					}
				}
			
			table.getCellFormatter().addStyleName(0, 0, "luc");// left upper corner
			table.getCellFormatter().addStyleName(0, header.getSize()-1, "ruc");// right upper corner
			table.getRowFormatter().addStyleName(0, "dwt tr th");
			
		} else if( headerT != null ){
			
			drawTableModel(headerT, 0);
			
		}
	}
	
	private void drawRow(DataModel dm, int ind, int begri){
		
		DataModel st = dm.getStyles();
		int groupCol = -1;
		if( this.data != null ) groupCol = this.data.getGroupColumn();
		
		int wi = 0;
		for(int i=0; i<dm.getSize(); i++){
			
			if( i != groupCol || dm.getColSpan(groupCol) > 1 ){
				
				if( dm.get(i) == null ) this.table.setWidget(begri, wi++, new Label(""));					
				else {
				
					DataModel colTypes = this.data.getColTypes();
				
					if( dm.isHeaderCellInfo(i) ){
						
						HeaderCellInfo hci = dm.getHeaderCellInfo(i);
						if( hci.getCellType() == TableModel.COLTYPE_WIDGET ) this.table.setWidget(begri, wi, (Widget)hci.getObj());
						else this.table.setWidget(begri, wi, new Label((String)hci.getObj()));
						table.getCellFormatter().setStyleName(begri, wi, "th");
					
					} else if( colTypes != null ){
						
						
						try{
							if( colTypes.getInt(i) == TableModel.COLTYPE_WIDGET ) this.table.setWidget(begri, wi, dm.getWidget(i));
							else this.table.setWidget(begri, wi, new Label(dm.getString(i)));
						} catch (java.lang.AssertionError ex){
//							Lib.alert("err: "+new Label(dm.getString(i)));
						}
						
						table.getCellFormatter().addStyleName(begri, wi, "dwt td");
						
					} else {
						this.table.setWidget(begri, wi, new Label(dm.getString(i)));
						table.getCellFormatter().addStyleName(begri, wi, "dwt td");
					}
	
					int cellCS = dm.getColSpan(i);
					int cellRS = dm.getRowSpan(i);
					
					if( cellCS > 1 ){
						this.table.getFlexCellFormatter().setColSpan(begri, wi, cellCS);
					}
					if( cellRS > 1 ){
						this.table.getFlexCellFormatter().setRowSpan(begri, wi, cellRS);
					}
					
					if( st != null ){
						String style = st.getString(i);
						if( !style.equals("") )	this.table.getCellFormatter().addStyleName(begri, wi, style);
						else this.table.getCellFormatter().setStyleName(begri, wi, "");
					}
					
					wi++;
				
				}
				
			}
			
		}
		
		table.getCellFormatter().addStyleName(begri, 0, "blc"); // begin line cell
		table.getCellFormatter().addStyleName(begri, dm.getSize()-1, "elc"); // end line cell
		table.getRowFormatter().addStyleName(begri, "dwt tr");
		
	}
	
	private void drawRow(int ind){
		int begri = 0;
		if( this.header != null ) begri++;
		begri += ind;
		if( ind < this.data.getSize() ){
			this.table.insertRow(begri);
		}
		DataModel dm = this.data.get(ind);
			
		this.drawRow(dm, ind, begri);
	}
	
	private int getTableBegri(){
		int begri = 0;
		if( header != null) begri++;
		else if( headerT != null ) begri += headerT.getSize();
		if( showSummaryTop ) begri++;
		return begri;
	}
	
	private DataModel initSummary(DataModel summrs){

		DataModel sum = new DataModel();
		
		if( summrs != null ){
			
			for(int col=0; col<summrs.getSize(); col++){
				
				Summator summr = (Summator) summrs.get(col);
				if( summr != null ) sum.add(summr.getZero());
				else sum.add(null);
				
			}

		}
		
		return sum;
	}
	
	private void sumWithSummators(DataModel sum, DataModel dm, DataModel summrs){
		for(int ci=0; ci<dm.getSize(); ci++){
			
			Summator summr = (Summator) summrs.get(ci);
			if(summr != null){
				sum.set(ci, summr.sum(sum.get(ci), dm.get(ci)));
			}
		}
	}
	
	private void roundSum(DataModel sum){
		Iterator<Object> i = sum.getIterator();
		int ii=0;
		while( i.hasNext() ){
			Object o = i.next();
			if( o != null && getColTypes().getInt(ii) == TableModel.COLTYPE_DOUBLE ){
				sum.set(ii, nf.format(Double.parseDouble((String)o)));
			} else sum.set(ii, o);
			ii++;
		}
	}
	
	private TableModel formatTable(TableModel intm){
		int grCol = this.data.getGroupColumn();
		boolean needGr = grCol != -1;
		boolean needSum = ( this.summators != null && (this.isShowSummaryTop() || this.isShowSummaryBottom()) );
		if(
			needGr ||
			needSum
		){

			TableModel outtm = new TableModel();
			
			DataModel sum = null;
			if( needSum ){
				sum = this.initSummary(this.getSummators());
				if( this.getSummaryStyles() != null ) sum.setStyles(this.getSummaryStyles());
			}
			
			Object prevGroup = null;
			boolean needGrSum = false;
			DataModel grsum = null;
			if( needGr ){
				this.data.checkGroup();
				needGrSum = this.getGroupSummators() != null && (this.isShowGroupSummaryTop() || this.isShowGroupSummaryBottom());
			}
			
			for(int ri=0; ri<intm.getSize(); ri++){
				
				DataModel dm = intm.get(ri);
				
				if( needGr ){
					Object group = dm.get(grCol);
					if( !group.equals(prevGroup) ){
						
						if( needGrSum && this.isShowGroupSummaryBottom() && grsum != null ) outtm.add(grsum);
	
						DataModel space = new DataModel();
						if( ((Integer)this.data.getColTypes().get(grCol)) == TableModel.COLTYPE_WIDGET ){
							space.add(new Label(""), dm.getSize()-1, 1);
						} else space.add("", dm.getSize()-1, 1);
						outtm.add(space);
						
						DataModel grHeader = new DataModel();
						DataModel grHeaderStyles = new DataModel();
						grHeader.add(group, dm.getSize()-1, 1);
						grHeaderStyles.add("tdbold");
						grHeader.setStyles(grHeaderStyles);
						outtm.add(grHeader);

						if( needGrSum ){
							grsum = this.initSummary(this.getGroupSummators());
							if( this.getGroupSummaryStyles() != null ) grsum.setStyles(this.getGroupSummaryStyles());
							if( this.isShowGroupSummaryTop() ) outtm.add(grsum);
						}
						
						prevGroup = group;
					}
					if( needGrSum ) this.sumWithSummators(grsum, dm, this.getGroupSummators());
				}
				
				outtm.add(dm);
				
				if( needSum ) this.sumWithSummators(sum, dm, this.getSummators());
				
			}
			
			if( sum != null ) roundSum(sum);
			if( grsum != null ) roundSum(grsum);
			
			if( needGrSum && this.isShowGroupSummaryBottom() && grsum != null ) outtm.add(grsum);
			if( this.isShowSummaryTop() ) outtm.insert(0, sum);
			if( this.isShowSummaryBottom() ) outtm.add(sum);
			
			return outtm;
				
		} else return intm;
	}
	
	private void drawTable(){
		this.drawTableModel(this.formattedData, this.getTableBegri());
	}

	private void drawTableModel(TableModel tm, int rowInd){
		int ri = rowInd;
		for(int tmind=0; tmind<tm.getSize(); tmind++){
			DataModel dm = tm.get(tmind);
			this.drawRow(dm, tmind, ri);
			ri++;
		}
		
		
		int last = table.getRowCount()-1;
		int lastCC = table.getCellCount(last)-1;
		if( last > -1 ){
			table.getCellFormatter().addStyleName(last, 0, "llc"); //left lower corner
			table.getCellFormatter().addStyleName(last, lastCC, "rlc"); //right lower corner
		}
		
		//mark last row tds
		for(int i=0; i<=lastCC; i++){
			table.getCellFormatter().addStyleName(last, i, "lr"); //last row
		}
	}
	
	private void removeHeader(){
		if( header != null ){
			table.removeRow(0);
		} else if( headerT != null ){
			for(int i=headerT.getSize()-1; i>=0 ; i--){
				table.removeRow(i);
			}
		}
	}
	
	private void processHeader(){
		if( 
			this.data.getSize() > 0
		) this.insertHeader();
		else this.drawHeader();
	}
	
	private void refreshHeader(){
		removeHeader();
		processHeader();
	}
	
	public void setHeader(TableModel tm){
		removeHeader();
		headerT = tm;
		header = null;
		processHeader();
	}
	
	public void setHeader(DataModel dm){
		removeHeader();
		header = dm;
		headerT = null;
		processHeader();
	}
	
	public void setHeaderStyles(DataModel dm){
		headerStyles = dm;
		refreshHeader();
	}
	
	public void clear(){
		this.table.removeAllRows();
		this.data.clear();
		this.header = null;
	}
	
	public void clearData(){
		for(int i=0;i<this.data.getSize();i++) this.table.removeRow(this.getTableBegri());
		this.data.clear();
	}
	
	public void setTableModel(TableModel tm){
		
		tm.setComparators(this.data.getComparators());
		tm.setColTypes(this.data.getColTypes());
		tm.setGroupColumn(this.data.getGroupColumn());

		if( this.prevSortCol != -1 ){
			tm.sort(this.prevSortCol, this.prevSortType);
			this.updateSortMarkers(this.prevSortCol, this.prevSortType);
		} else {
			tm.checkGroup();
		}
		
		TableModel ftm = this.formatTable(tm);
		
		if( this.formattedData.getSize() > 0 ) this.clearRows(this.getTableBegri(), this.formattedData.getSize(), ftm.getSize());
		this.data = tm;
		this.formattedData = ftm;
		
		this.drawTable();
			
	}
	
	public void addRow(DataModel dm){
		this.data.add(dm);
	}
	
	public HandlerRegistration addClickHandler(ClickHandler handler){
		return this.table.addClickHandler(handler);
	}
	
	public Cell getCellForEvent(ClickEvent event){
		return this.table.getCellForEvent(event);
	}

	public DataModel getColTypes() {
		return this.data.getColTypes();
	}

	public void setColTypes(DataModel colTypes) {
		this.data.setColTypes(colTypes);
		refreshHeader();
	}
	
	public void sort(int col){
		if( col != this.prevSortCol ){
			this.sort(col, TableModel.SORT_ASC);
		} else
		if( this.prevSortType == TableModel.SORT_NONE ){
			this.sort(col, TableModel.SORT_ASC);
		} else if(prevSortType == TableModel.SORT_ASC ){
			this.sort(col, TableModel.SORT_DESC);
		} else if(prevSortType == TableModel.SORT_DESC ){
			this.sort(col, TableModel.SORT_ASC);
		}

	}
	
	private void updateSortMarkers(int col, int sortType){
		if( this.prevSortCol != -1 ) this.sortMarkers.get(this.prevSortCol).setHTML("");
		this.sortMarkers.get(col).setHTML(sortType==TableModel.SORT_ASC?"&#9660;":"&#9650;");
	}
	
	public void sort(int col, int sortType){
//		Lib.Alert("sort: "+col+" "+sortType);
		int sortcol = col;
		if( this.data.getGroupColumn() != -1 && this.data.getGroupColumn() <= col ) sortcol++;
		this.data.sort(sortcol, sortType);
//		Lib.Alert("a: "+this.getTableBegri()+" "+this.data.getSize());
		this.clearRows(this.getTableBegri(), this.data.getSize(), this.data.getSize());
//		Lib.Alert("b");
		this.drawTable();
//		Lib.Alert("c");
		this.updateSortMarkers(col, sortType);
		this.prevSortCol = col;
		this.prevSortType = sortType;
	}
	
	public DataModel getComparators() {
		return this.data.getComparators();
	}

	public void setComparators(DataModel comparators) {
		this.data.setComparators(comparators);
		this.drawHeader();
	}
	
	public int getGroupColumn() {
		return this.data.getGroupColumn();
	}

	public void setGroupColumn(int groupColumn) {
		this.data.setGroupColumn(groupColumn);
	}

	public boolean isShowSummaryTop() {
		return showSummaryTop;
	}

	public void setShowSummaryTop(boolean showSummaryTop) {
		this.showSummaryTop = showSummaryTop;
	}

	public boolean isShowSummaryBottom() {
		return showSummaryBottom;
	}

	public void setShowSummaryBottom(boolean showSummaryBottom) {
		this.showSummaryBottom = showSummaryBottom;
	}

	public DataModel getSummators() {
		return summators;
	}

	public void setSummators(DataModel summators) {
		this.summators = summators;
	}

	public DataModel getSummaryStyles() {
		return summaryStyles;
	}

	public void setSummaryStyles(DataModel summaryStyles) {
		this.summaryStyles = summaryStyles;
	}

	public boolean isShowGroupSummaryTop() {
		return showGroupSummaryTop;
	}

	public void setShowGroupSummaryTop(boolean showGroupSummaryTop) {
		this.showGroupSummaryTop = showGroupSummaryTop;
	}

	public boolean isShowGroupSummaryBottom() {
		return showGroupSummaryBottom;
	}

	public void setShowGroupSummaryBottom(boolean showGroupSummaryBottom) {
		this.showGroupSummaryBottom = showGroupSummaryBottom;
	}

	public DataModel getGroupSummators() {
		return groupSummators;
	}

	public void setGroupSummators(DataModel groupSummators) {
		this.groupSummators = groupSummators;
	}

	public DataModel getGroupSummaryStyles() {
		return groupSummaryStyles;
	}

	public void setGroupSummaryStyles(DataModel groupSummaryStyles) {
		this.groupSummaryStyles = groupSummaryStyles;
	}
	
}
