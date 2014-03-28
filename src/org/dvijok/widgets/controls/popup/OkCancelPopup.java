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

package org.dvijok.widgets.controls.popup;

import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;

public class OkCancelPopup {

	private Popup popup;
	private OkCancelFormForPopup form;
	
	public OkCancelPopup(OkCancelFormForPopup form){
		initPopup(form);
	}
	
	public OkCancelPopup(String question, String okTitle, String cancelTitle){
		OkCancelForm f = new OkCancelForm();
		f.setHTML(question);
		f.setOkTitle(okTitle);
		f.setCancelTitle(cancelTitle);
		initPopup(f);
	}
	
	private void initPopup(OkCancelFormForPopup form){
		popup = new Popup();
		this.form = form;
		popup.setContent(form.asWidget());
	}
	
	public void setHTML(String html){
		form.setHTML(html);
	}
	
	public void setHideOnAction(){
		
		CustomEventListener l = new CustomEventListener() {
			@Override
			public void customEventOccurred(CustomEvent evt) {
				setVisible(false);
			}};
			
		addOkListener(l);
		addCancelListener(l);
		addCloseListener(l);
	}

	public OkCancelFormForPopup getForm() {
		return form;
	}

	public void setVisible(boolean visible){
		popup.setVisible(visible);
	}
	
	public void addCloseListener(CustomEventListener listener){
		popup.addCloseListener(listener);
	}
	
	public void removeCloseListener(CustomEventListener listener){
		popup.addCloseListener(listener);
	}
	
	public void addOkListener(CustomEventListener listener){
		form.addOkListener(listener);
	}
	
	public void removeOkListener(CustomEventListener listener){
		form.removeOkListener(listener);
	}
	
	public void addCancelListener(CustomEventListener listener){
		form.addCancelListener(listener);
	}
	
	public void removeCancelListener(CustomEventListener listener){
		form.removeCancelListener(listener);
	}
	
}
