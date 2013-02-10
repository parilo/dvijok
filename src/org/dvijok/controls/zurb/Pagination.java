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

import org.dvijok.controls.Element;
import org.dvijok.controls.LI;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class Pagination extends ComplexPanel {

	private Element ul;
	private Anchor left;
	private Anchor right;
	private Anchor middle1;
	private Anchor middle2;
	
	private int numberOfAllElements;
	private int displayCount;
	private int currentSelected;
	private CustomEventTool pageSelected;
	private ClickHandler pageClicked;
	
/*
	<ul class="pagination">
	  <li class="arrow unavailable"><a href="">&laquo;</a></li>
	  <li class="current"><a href="">1</a></li>
	  <li><a href="">2</a></li>
	  <li><a href="">3</a></li>
	  <li><a href="">4</a></li>
	  <li class="unavailable"><a href="">&hellip;</a></li>
	  <li><a href="">12</a></li>
	  <li><a href="">13</a></li>
	  <li class="arrow"><a href="">&raquo;</a></li>
	</ul>
*/
	
	public Pagination(){
		
		pageSelected = new CustomEventTool();
		
		ul = (Element) Document.get().createULElement().cast();
		ul.addClassName("pagination");

		setElement(ul);
		
		left = new Anchor();
		left.setHTML("&laquo;");
		
		right = new Anchor();
		right.setHTML("&raquo;");
		
		middle1 = new Anchor();
		middle1.setHTML("&hellip;");
		
		middle2 = new Anchor();
		middle2.setHTML("&hellip;");
		
		pageClicked = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Anchor a = (Anchor) event.getSource();
				pageSelected.invokeListeners(Lib.getInt(a.getText()));
			}};
	}

	public void setParameters(int numberOfAllElements, int displayCount, int currentSelected){
		this.numberOfAllElements = numberOfAllElements;
		this.displayCount = displayCount;
		this.currentSelected = currentSelected;
		draw();
	}
	
	private int mode=1; //1 - normal, 2 - links
	private String prefix;
	
	public void setLinksMode(String prefix){
		this.prefix = prefix;
		mode = 2;
		draw();
	}
	
	public void setNormalMode(){
		mode = 1;
		draw();
	}
	
	private Widget getPageAnchor(String pageNumber){
		
		if( mode == 2 ){
			
			Hyperlink l = new Hyperlink();
			l.setText(pageNumber);
			l.setTargetHistoryToken(prefix+pageNumber);
			return l;
			
		} else {
			
			Anchor a = new Anchor(pageNumber);
			a.addClickHandler(pageClicked);
			return a;
			
		}
	}
	
	private void draw(){
		
		ul.clear();
		
		if( numberOfAllElements > 1 ){
		
			LI li;
//			Widget a;
			
			int leftBorder = Math.max(currentSelected - displayCount/2, 1);
			int rightBorder = Math.min(currentSelected + displayCount/2, numberOfAllElements);
			int count = rightBorder - leftBorder;
			if( count < displayCount ){
				if( leftBorder == 1 ) rightBorder = Math.min(displayCount, numberOfAllElements);
				else if( rightBorder == numberOfAllElements ) leftBorder = Math.max(numberOfAllElements-displayCount, 1);
			}
			
			li = new LI(left);
			li.addStyleName("arrow");
			if( currentSelected == 1 ) li.addStyleName("unavailable");
			add(li, ul);
			
//			a = new Anchor("1");
//			a.addClickHandler(pageClicked);
			li = new LI(getPageAnchor("1"));
			if( 1 == currentSelected ) li.addStyleName("current");
			add(li, ul);

			if( leftBorder > 3 ) add(new LI(middle1), ul);

			int begi = Math.max(leftBorder, 2);
			int endi = Math.min(rightBorder, numberOfAllElements-2);
			if( begi == 3 ) begi=2;
			if( endi == numberOfAllElements-2 ) endi = numberOfAllElements-1;
			for( int i=begi; i<=endi; i++ ){
//				a = new Anchor(""+i);
//				a.addClickHandler(pageClicked);
				li = new LI(getPageAnchor(""+i));
				if( i == currentSelected ){
					li.addStyleName("current");
				}
				add(li, ul);
			}
			
			if( rightBorder < numberOfAllElements-2 ) add(new LI(middle2), ul);
			
//			a = new Anchor(""+numberOfAllElements);
//			a.addClickHandler(pageClicked);
			li = new LI(getPageAnchor(""+numberOfAllElements));
			if( numberOfAllElements == currentSelected ) li.addStyleName("current");
			add(li, ul);
			
			li = new LI(right);
			li.addStyleName("arrow");
			if( currentSelected == numberOfAllElements ) li.addStyleName("unavailable");
			add(li, ul);
			
//			if( numberOfAllElements <= 2*displayCount ){
//				
//				
//				for(int i=1; i<=numberOfAllElements; i++ ){
//					
//					Anchor a = new Anchor(""+i);
//					a.addClickHandler(pageClicked);
//					li = new LI(a);
//					if( i == currentSelected ){
//						li.addStyleName("current");
//					}
//					add(li, ul);
//					
//				}
//				
//			} else {
//				
//				for( int i=1; i<=displayCount; i++ ){
//	
//					Anchor a = new Anchor(""+i);
//					a.addClickHandler(pageClicked);
//					li = new LI(a);
//					if( i == currentSelected ){
//						li.addStyleName("current");
//					}
//					add(li, ul);
//					
//				}
//				
//				add(new LI(middle), ul);
//				
//				for( int i=numberOfAllElements-displayCount+1; i<=numberOfAllElements; i++ ){
//	
//					Anchor a = new Anchor(""+i);
//					a.addClickHandler(pageClicked);
//					li = new LI(a);
//					if( i == currentSelected ){
//						li.addStyleName("current");
//					}
//					add(li, ul);
//					
//				}
//				
//			}
//	
//			li = new LI(right);
//			li.addStyleName("arrow");
//			if( currentSelected == numberOfAllElements ) li.addStyleName("unavailable");
//			add(li, ul);
		
		}
		
	}
	
	public void setSelected(int pageNumber){
		currentSelected = pageNumber;
		draw();
	}
	
	public void setNumberOfAllElements(int numberOfAllElements){
		this.numberOfAllElements = numberOfAllElements;
		draw();
	}
	
	public void addPageSelectedListener(CustomEventListener listener){
		pageSelected.addCustomEventListener(listener);
	}
	
	public void removePageSelectedListener(CustomEventListener listener){
		pageSelected.removeCustomEventListener(listener);
	}
	
}


