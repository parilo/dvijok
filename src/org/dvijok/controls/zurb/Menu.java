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

package org.dvijok.controls.zurb;

import java.util.HashMap;

import org.dvijok.controls.LI;
import org.dvijok.controls.UL;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public class Menu extends ComplexPanel {

	private UL ul;
	private LI first;
	private LI last;
	private LI active;
	private HashMap<Anchor, LI> lis;
	private boolean needLast;
	
/*
<ul id="mainnav" class="nav-bar">

	<li id="mainnavfirst" class="has-flyout">
	  <a href="javascript:;">Вход</a>
	  <a href="javascript:;" class="flyout-toggle"><span> </span></a>
	  <div class="enterpane flyout large" id="dvijokw" name="authcombo"></div>
	</li>

	<li class="active"><a href="javascript:;">Объявления</a></li>
	<li class=""><a href="addobject.html">Разместить объявление</a></li>
	<li class=""><a href="javascript:;">Нашел квартиру?</a></li>
  
  
	<li id="mainnavlast" class="has-flyout">
	  <a href="javascript:;">Полезное</a>
	  <a href="javascript:;" class="flyout-toggle"><span> </span></a>
	  <ul class="flyout right readpane">
	    <li><a href="http://find-home.ru/contract.doc">Пример договора аренды</a></li>
	  </ul>
	</li>
	
</ul>
*/
	
	public Menu(String ulclassname){
		
		needLast = true;
		lis = new HashMap<Anchor, LI>();
		
		Element div = DOM.createDiv();
		
		ul = new UL();
		ul.addStyleName("nav-bar");
		ul.addStyleName(ulclassname);

		setElement(div);
		add(ul, getElement());
		
	}
	
	
	
	
	public void addItem(Anchor a){
		addItem(a, false);
	}
	
	public void insertItem(Anchor a, int beforeIndex){
		insertItem(a, false, -1);
	}
	
	public void addItem(Anchor a, boolean isActive){
		insertItem(a, isActive, -1);
	}
	
	public void insertItem(Anchor a, boolean isActive, int beforeIndex){
		LI li = new LI();
		if( isActive ) setActive(li);
		initBorderItems(li);
		registerAnchor(a, li);

		if( beforeIndex == -1 ) add(li, ul.getElement());
		else insert(li, ul.getElement(), beforeIndex, true);
		
		add(a, li.getElement());
	}
	
	
	
	public void addItem(String label, boolean isActive){
		insertItem(label, isActive, -1);
	}
		
	public void insertItem(String label, boolean isActive, int beforeIndex){
		LI li = new LI();
		if( isActive ) setActive(li);
		initBorderItems(li);

		Anchor a = new Anchor(label);
		registerAnchor(a, li);
		
		if( beforeIndex == -1 ) add(li, ul.getElement());
		else insert(li, ul.getElement(), beforeIndex, true);
		
		add(a, li.getElement());
	}
	
	
	
	public void addItem(String label, boolean isActive, String href){
		insertItem(label, isActive, href, -1);
	}
	
	public void insertItem(String label, boolean isActive, String href, int beforeIndex){
		LI li = new LI();
		if( isActive ) setActive(li);
		initBorderItems(li);

		Anchor a = new Anchor(label, false, href);
		registerAnchor(a, li);
		
		if( beforeIndex == -1 ) add(li, ul.getElement());
		else insert(li, ul.getElement(), beforeIndex, true);
		
		add(a, li.getElement());
	}

	
	
	
	/*
	<li id="mainnavfirst" class="has-flyout">
	  <a href="javascript:;">Вход</a>
	  <a href="javascript:;" class="flyout-toggle"><span> </span></a>
	  <div class="enterpane flyout large" id="dvijokw" name="authcombo"></div>
	</li>
	*/
	public void addItemFlayoutLarge(String label, boolean isActive, final Dwidget div, boolean right){
		insertItemFlayoutLarge(label, isActive, div, right, -1);
	}
	
	public void insertItemFlayoutLarge(String label, boolean isActive, final Dwidget div, boolean right, int beforeIndex){
		LI li = new LI();
		if( isActive ) setActive(li);
		li.addStyleName("has-flyout");
		initBorderItems(li);
		
		li.addMouseOverListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				div.getElement().getStyle().setDisplay(Display.BLOCK);
			}
		});
		
		li.addMouseOutListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				div.getElement().getStyle().setDisplay(Display.NONE);
			}
		});

		Anchor a = new Anchor(label);
		registerAnchor(a, li);
		
		Anchor a2 = new Anchor();
		InlineHTML span = new InlineHTML("");
		
		div.addStyleName("flyout");
		div.addStyleName("large");
		if( right ) div.addStyleName("right");
		
		if( beforeIndex == -1 ) add(li, ul.getElement());
		else insert(li, ul.getElement(), beforeIndex, true);
		
		add(a, li.getElement());
		add(a2, li.getElement());
		add(span, a2.getElement());
		add(div, li.getElement());
	}

	
	
	
	/*
	<li id="mainnavlast" class="has-flyout">
	  <a href="javascript:;">Полезное</a>
	  <a href="javascript:;" class="flyout-toggle"><span> </span></a>
	  <ul class="flyout right readpane">
	    <li><a href="http://find-home.ru/contract.doc">Пример договора аренды</a></li>
	  </ul>
	</li>
	*/
	public void addItemFlayout(String label, boolean isActive, final UL subul, boolean right){
		insertItemFlayout(label, isActive, subul, right, -1);
	}
	
	public void insertItemFlayout(String label, boolean isActive, final UL subul, boolean right, int beforeIndex){
		LI li = new LI();
		if( isActive ) setActive(li);
		li.addStyleName("has-flyout");
		initBorderItems(li);
		
		li.addMouseOverListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				subul.getElement().getStyle().setDisplay(Display.BLOCK);
			}
		});
		
		li.addMouseOutListener(new CustomEventListener(){
			@Override
			public void customEventOccurred(CustomEvent evt) {
				subul.getElement().getStyle().setDisplay(Display.NONE);
			}
		});

		Anchor a = new Anchor(label);
		registerAnchor(a, li);
		
		Anchor a2 = new Anchor();
		InlineHTML span = new InlineHTML("");
		
		subul.addStyleName("flyout");
		if( right ) subul.addStyleName("right");
		
		if( beforeIndex == -1 ) add(li, ul.getElement());
		else insert(li, ul.getElement(), beforeIndex, true);
		
		add(a, li.getElement());
		add(a2, li.getElement());
		add(span, a2.getElement());
		add(subul, li.getElement());
	}
	
	
	
	
	private void initBorderItems(LI added){
		if( first == null ){
			first = added;
			added.addStyleName("first");
		}
		
		if( needLast ){
			if( last != null ){
				last.removeStyleName("last");
			}
			last = added;
			added.addStyleName("last");
		}
	}

	public boolean isNeedLast() {
		return needLast;
	}

	public void setNeedLast(boolean needLast) {
		this.needLast = needLast;
	}

	private void setActive(LI li) {
		if( active != null ) active.removeStyleName("active");
		active = li;
		li.addStyleName("active");
	}
	
	public void setActive(Anchor a){
		if( lis.containsKey(a) ) setActive(lis.get(a));
	}

	private void registerAnchor(Anchor a, LI li){
		lis.put(a, li);
	}
	
	public void clearActive(){
		if( active != null ){
			active.removeStyleName("active");
			active = null;
		}
	}
	
	
}
