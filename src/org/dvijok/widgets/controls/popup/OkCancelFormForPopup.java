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

import org.dvijok.event.CustomEventListener;

import com.google.gwt.user.client.ui.IsWidget;

public interface OkCancelFormForPopup extends IsWidget {
	
	public void setHTML(String html);
	
	public void addOkListener(CustomEventListener listener);
	public void removeOkListener(CustomEventListener listener);
	
	public void addCancelListener(CustomEventListener listener);
	public void removeCancelListener(CustomEventListener listener);
	
}