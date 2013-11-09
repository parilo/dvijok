// author: Pechenko Anton Vladimirovich. 2012

package org.dvijok.event;

import java.util.ArrayList;

import org.dvijok.lib.Lib;
import org.dvijok.resources.Resources;
import org.dvijok.rpc.DBObject;
import org.dvijok.widgets.SubPanel;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class CustomParamsEventListener implements CustomEventListener {

	private Object obj;
	
	public CustomParamsEventListener(Object obj){
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
