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
import java.util.LinkedList;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.handlers.RequestHandler;
import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.rpc.DBObject;
import org.dvijok.widgets.busy.BigBusy;
import org.dvijok.widgets.busy.Busy;
import org.dvijok.widgets.busy.SmallBusy;
import org.dvijok.widgets.fx.gfx.FadeOut;
import org.dvijok.widgets.fx.gfx.GFX;
import org.dvijok.widgets.fx.gfx.VerticalExpansion;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Dwidget_1_0 extends Composite {
	
	private String tmplUrl;
	private String tmplData;
	private ArrayList<DBObject> params;
//	private HashMap<String, HTMLPanel> modes;
	private HashMap<String, String> modes;
	private String dbid;
	private String dwid;
	private boolean inline;
	private LinkedList<String> tmplQueue;
	private boolean isloading;
	private boolean debug;
	
	private SimplePanel maincont;
	private HTMLPanel main;
	
	private SubPanel panel;
	private GFX startAnimation = null;

	public Dwidget_1_0(String templUrl) {
		this(templUrl, false);
	}
	
	public Dwidget_1_0(String templUrl, boolean debug) {
		this.debug = debug; 
//		this.modes = new HashMap<String, HTMLPanel>();
		this.modes = new HashMap<String, String>();
		tmplQueue = new LinkedList<String>();
		isloading = false;
		this.beforeTmplInit();
		this.init(templUrl);
	}

	public Dwidget_1_0(String templUrl, SubPanel p) {
		this(templUrl, p, false);
	}
	
	public Dwidget_1_0(String templUrl, SubPanel p, boolean debug) {
		this.debug = debug; 
//		this.modes = new HashMap<String, HTMLPanel>();
		this.modes = new HashMap<String, String>();
		tmplQueue = new LinkedList<String>();
		isloading = false;
		this.panel = p;
		this.readParams();
		this.readDbid();
		this.readDwid();
		this.panel.clear();
		this.panel.getElement().setInnerHTML("");
		this.beforeTmplInit();
		this.init(templUrl);
	}
	
	protected void beforeTmplInit(){}
	public void reinit(){}
	public boolean needAuthReinit(){ return false; }
	
	private void init(final String templUrl){
		inline = false;
		this.maincont = new SimplePanel();
		this.initWidget(this.maincont);
		loadTmpl(templUrl);
	}
	
	private void loadTmpl(String url){
		tmplQueue.add(url);
		this.getTmpl();
	}
	
	private void getTmpl(){

		if( isloading == false && tmplQueue.size() > 0 ){
			
			isloading = true;
			
			final String url = tmplQueue.poll();
			Resources.getInstance().tmpls.getTemplate(url, new CustomEventListener(){
	
				@Override
				public void customEventOccurred(CustomEvent evt) {
					String text = (String)evt.getSource();
					if( evt.isFailed() ){
						Lib.alert("cannot get template "+url+" : "+text);
					} else {
						tmplUrl = url;
						tmplData = text;
						modes.put(tmplUrl, tmplData);
						createGUI();
					}
				}
			});
		
		}
		
	}
	
	private void readParams(){
//		this.params = Resources.getInstance().loader.getParams(this.panel);
	}
	
	public ArrayList<DBObject> getParams(){
		return this.params;
	}
	
	private void readDbid(){
//		this.dbid = Resources.getInstance().loader.getAttribute(this.panel, "dbid");
	}
	
	private void readDwid(){
//		this.dwid = Resources.getInstance().loader.getAttribute(this.panel, "dwid");
	}
	
	public String getDbid(){
		return this.dbid;
	}
	
	public String getDwid(){
		return this.dwid;
	}
	
	public HTMLPanel getHTMLPanel(){
		return this.main;
	}
	
	protected void createGUI(){
		this.initTmpl();
		this.attachTmpl();
		continiueLoadTmpl();
	}
	
	protected void continiueLoadTmpl(){
		isloading = false;
		getTmpl();
	}
	
	protected void initTmpl(){
		this.main = new HTMLPanel(tmplData);
		main.addStyleName("tmplcont");
		if( inline ) main.getElement().getStyle().setDisplay(Display.INLINE);
//		this.modes.put(this.tmplUrl, this.main);
	}
	
	protected void attachTmpl(){
		this.maincont.setWidget(this.main);
//		Resources.getInstance().loader.loadNew();
//		Resources.getInstance().loader.load(this);
	}
	
	protected void changeTmpl(String url){
		if( this.modes.containsKey(url) ){
//Lib.alert(""+this.modes.get(url));
//			this.main = this.modes.get(url);
//			this.maincont.setWidget(this.main);
			tmplUrl = url;
			tmplData = modes.get(url);
			createGUI();
		} else this.loadTmpl(url);
	}
	
	protected String getTmplUrl() {
		return tmplUrl;
	}
	
	public void redraw(){
		createGUI();
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
			
			Dwidget_1_0 bp = (Dwidget_1_0) busypane;
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
	
	public void centerVertical(){
		getElement().getStyle().setPosition(Position.RELATIVE);
		getElement().getStyle().setTop(50, Unit.PX);
		getElement().getStyle().setMarginTop(getOffsetHeight()/2, Unit.PX);
	}
	
	public void centerWidgetPanelVertical(){
		this.main.getElement().getStyle().setPosition(Position.RELATIVE);
		this.main.getElement().getStyle().setTop(50, Unit.PX);
		this.main.getElement().getStyle().setMarginTop(this.main.getOffsetHeight()/2, Unit.PX);
	}
	
	public void setInline(boolean isInline){
		inline = isInline;
		maincont.getElement().getStyle().setDisplay(Display.INLINE);
	}
	
	public void setHeightMain(String height){
		main.setHeight(height);
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
