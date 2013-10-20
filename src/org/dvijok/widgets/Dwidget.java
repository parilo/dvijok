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

package org.dvijok.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.widgets.busy.BigBusy;
import org.dvijok.widgets.busy.Busy;
import org.dvijok.widgets.busy.SmallBusy;
import org.dvijok.widgets.fx.gfx.FadeOut;
import org.dvijok.widgets.fx.gfx.GFX;
import org.dvijok.widgets.fx.gfx.VerticalExpansion;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class Dwidget extends /*ComplexPanel*/Composite {
	
	private String tmplUrl;
	private String tmplData;
	private int loadCounter;
	private boolean loaded;
	
	private SimplePanel fakeContent;
	private HTMLPanel loadingPanel;
	
	private GFX startAnimation = null;
	
	private CustomEventListener loadedL;
	private CustomEventListener subDwidgetsLoadedL;
	
	public Dwidget(String templUrl) {
		this.init(templUrl);
	}
	
	public void setLoadedListener(CustomEventListener loadedListener){
		if( loaded ){
			loadedL = null;
			loadedListener.customEventOccurred(null);
		} else {
			loadedL = loadedListener;
		}
	}
	
	public void _afterLoad(){
	    onAttach();
	    RootPanel.detachOnWindowClose(this);
	}
	
	protected ArrayList<Dwidget> beforeLoadingSubDwidgets(HTMLPanel htmlPanel){return new ArrayList<Dwidget>();}
	
	private void init(String templUrl){
		loaded = false;
		loadCounter = 0;
		subDwidgetsLoadedL = new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				subDwidgetsCountLoaded++;
				tryPlaceElements();
			}};
		
		initInternals();
			
		elements = new ArrayList<com.google.gwt.dom.client.Element>();
		fakeContent = new SimplePanel();
		fakeContent.getElement().getStyle().setPosition(Position.ABSOLUTE);
		fakeContent.getElement().getStyle().setDisplay(Display.NONE);
		this.initWidget(fakeContent);
		loadTmpl(templUrl);
	}
	
	protected abstract void initInternals();
	
	private void loadTmpl(final String url){

		Resources.getInstance().tmpls.getTemplate(url, new CustomEventListener(){

			@Override
			public void customEventOccurred(CustomEvent evt) {
				String text = (String)evt.getSource();
				if( evt.isFailed() ){
					Lib.alert("cannot get template "+url+" : "+text);
				} else {
					tmplUrl = url;
					tmplData = text;
					tmplLoaded();
				}
			}
		});
		
	}
	
	private void tryLoad(int loadCounterValue){
		
		if( loadCounterValue == 2 ){
			//this Dwidget was attached and tmeplate is loaded
	
			//loading sub widgets and sub dwidgets
			//than getting dom elements from fakeContent 
			//and putting it in fakeContent parent element
			
			loadingPanel = new HTMLPanel(tmplData);
			fakeContent.setWidget(loadingPanel);
			ArrayList<Dwidget> subDw = beforeLoadingSubDwidgets(loadingPanel);
			subDw.addAll(Resources.getInstance().loader.load(loadingPanel));

			elementsPlaced = false;
			subDwidgetsCount = subDw.size();
			subDwidgetsCountLoaded = 0;
			Iterator<Dwidget> i = subDw.iterator();
			while( i.hasNext() ){
				i.next().setLoadedListener(subDwidgetsLoadedL);
			}
			
			tryPlaceElements();
			
		}
		
	}
	
	private boolean elementsPlaced;
	private int subDwidgetsCount;
	private int subDwidgetsCountLoaded;
	
	private void tryPlaceElements(){

		if( !elementsPlaced && subDwidgetsCount == subDwidgetsCountLoaded ){
			storeElements();
			placeElements();
		
			elementsPlaced = true;
			loaded = true;
			if( loadedL != null ) loadedL.customEventOccurred(null);
			super.onDetach();
			super.onAttach();
		}
		
	}
	
	private void tmplLoaded(){
		tryLoad(++loadCounter);
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		tryLoad(++loadCounter);
	}
	
	private ArrayList<com.google.gwt.dom.client.Element> elements;
	
	private void storeElements(){
		
		elements.clear();
		
		com.google.gwt.dom.client.Element htmlpanelel = loadingPanel.getElement();
		com.google.gwt.dom.client.Element subel;
		while( (subel = htmlpanelel.getFirstChildElement()) != null ){
			elements.add(subel);
			subel.removeFromParent();
		}
		
	}
	
	private void placeElements(){

		com.google.gwt.dom.client.Element parent = fakeContent.getElement().getParentElement();
		com.google.gwt.dom.client.Element prevel = fakeContent.getElement();
		Iterator<com.google.gwt.dom.client.Element> i = elements.iterator();
		while( i.hasNext() ){
			com.google.gwt.dom.client.Element subel = i.next();
			parent.insertAfter(subel, prevel);
			prevel = subel;
		}
		
	}
	
	private void removeElements(){
		com.google.gwt.dom.client.Element parent = fakeContent.getElement().getParentElement();
		Iterator<com.google.gwt.dom.client.Element> i = elements.iterator();
		while( i.hasNext() ){
			com.google.gwt.dom.client.Element el = i.next();
			parent.removeChild(el);
		}
	}
	
	protected void changeTmpl(String url){
		removeElements();
		loadCounter--;
		loadTmpl(url);
	}
	
	protected String getTmplUrl() {
		return tmplUrl;
	}
	
	public void redraw(){
		changeTmpl(tmplUrl);
	}

	private Busy busypane;
	
	public Busy getBusy(){
		return busypane;
	}
	
	public void setBusy(boolean busy){
		if( busy ){
			int height = getOffsetHeight();
			if( height > 150 ) busypane = new BigBusy();
			else busypane = new SmallBusy();
			
			Dwidget bp = (Dwidget) busypane;
			bp.setWidth(getOffsetWidth()+"px");
			bp.setHeight(height+"px");
//			busypane.setHeight((getOffsetHeight()-20)+"px");
			bp.getElement().getStyle().setLeft(getAbsoluteLeft(), Unit.PX);
			bp.getElement().getStyle().setTop(getAbsoluteTop(), Unit.PX);

//			busypane.setWidth(getElement().getClientWidth()+"px");
//			busypane.setHeight(getElement().getClientHeight()+"px");
//			busypane.getElement().getStyle().setLeft(getElement().getAbsoluteLeft(), Unit.PX);
//			busypane.getElement().getStyle().setTop(getElement().getAbsoluteTop(), Unit.PX);
			
			Resources.getInstance().addToTmp(bp);
		} else {
			
			if( busypane != null ){
				Widget bp = (Widget) busypane;
				FadeOut fx = new FadeOut();
				fx.setWidget(bp);
				DBObject props = new DBObject();
				props.put("startOpacity", Double.toString(0.5));
				fx.setProperties(props);
				fx.start();
				fx.addEndListener(new CustomEventListener(){
					@Override
					public void customEventOccurred(CustomEvent evt) {
						Widget bp = (Widget) busypane;
						Resources.getInstance().removeFromTmp(bp);
					}
				});
				
			}
		}
	}

	/*
	 * animation
	 */
	public void performAnimation(GFX gfx){
		addStyleName("tmp");
		gfx.setWidget(this);
		gfx.init();
		removeStyleName("tmp");
		gfx.start();
	}
	
	public void setStartAnimation(GFX gfx){
		gfx.setWidget(this);
		startAnimation = gfx;
	}
	
	public void beforeAttach(){
		if( startAnimation != null ) {
			addStyleName("tmp");
		}
	}
	
	public void afterAttach(){
		if( startAnimation != null ) {
			startAnimation.init();
			removeStyleName("tmp");
			startAnimation.start();
		}
	}

}
