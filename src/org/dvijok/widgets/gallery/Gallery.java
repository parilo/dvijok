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

package org.dvijok.widgets.gallery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.dvijok.controls.DivPanel;
import org.dvijok.db.DBArray;
import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Gallery extends SubPanelsDwidget {

	private GalleryBigItem big;
//	private ArrayList<GalleryItem> items;
	private DivPanel itemsPanel;
	
	private GalleryItemFactory factory;
	
	private GalleryItemModel selected; 
	private GalleryModel model;
	private CustomEventListener itemSelected;
	
	public Gallery(){
		super("tmpl/widgets/gallery/gallery.html");
	}
	
	public Gallery(String tmpl){
		super(tmpl);
	}
	
	public GalleryModel getModel() {
		return model;
	}

	public void setModel(GalleryModel model) {
		this.model = model;
	}

	private void draw(){

		itemsPanel.clear();
		big = null;

		DBArray dba = model.getDBA();
		Iterator<Serializable> i = dba.iterator();
		boolean first = true;
		
		while( i.hasNext() ){
			DBObject ph = (DBObject) i.next();
			
			GalleryItemModel itemModel = new GalleryItemModel();
			itemModel.setDB0(ph);
			if( first ){
				itemModel.setFirst(true);
				first = false;
			}
			if( !i.hasNext() ) itemModel.setLast(true);
			
			
			if( itemModel.equals(selected) ){
				big = factory.getBigItem(itemModel);
			}
			GalleryItem gitem = factory.getItem(itemModel);
			gitem.addItemSelectedListsner(itemSelected);
			itemsPanel.addWidget(gitem.asWidget());
		}
		
		if( big == null )
		if( dba.size() > 0 ){
			GalleryItemModel itemModel = new GalleryItemModel();
			itemModel.setDB0((DBObject)dba.get(0));
			big = factory.getBigItem(itemModel);
		}
		
		redraw();
			
	}
	
	public void clear(){
		itemsPanel.clear();
		big = null;
		redraw();
	}

	public GalleryItemFactory getFactory() {
		return factory;
	}

	public void setFactory(GalleryItemFactory factory) {
		this.factory = factory;
	}

	@Override
	protected void initInternals() {
		
		selected = null;
		
		model = new GalleryModel();
		model.addPhotoChangedListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				draw();
			}});
		
		itemsPanel = new DivPanel();
		
		itemSelected = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				selected = (GalleryItemModel) evt.getSource();
				draw();
			}};
	}

	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("big") ) return big==null?new Label(""):big.asWidget();
		else if( dwname.equals("items") ) return itemsPanel;

		else
			return null;
	}
	
}
