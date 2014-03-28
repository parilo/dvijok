// author: Pechenko Anton Vladimirovich. 2012

package org.dvijok.widgets.controls.popup;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.widgets.Dwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class OkCancelForm extends Dwidget implements OkCancelFormForPopup {
	
	private HTML text;
	private Button ok;
	private Button cancel;
	
	private CustomEventTool okET; 
	private CustomEventTool cancelET; 
	
	public OkCancelForm(){
		super("tmpl/widgets/popup/okcancel.html");
	}
	
	private OkCancelForm getMe(){ return this; }
	
	public void setHTML(String html){
		text.setHTML(html);
	}
	
	public void setOkTitle(String title){
		ok.setText(title);
	}
	
	public void setCancelTitle(String title){
		cancel.setText(title);
	}
	
	@Override
	protected void initInternals() {
		
		okET = new CustomEventTool();
		cancelET = new CustomEventTool();
		
		text = new HTML();
		
		ok = new Button("Ok");
		ok.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				okET.invokeListeners(getMe());
			}});
		
		cancel = new Button("Cancel");
		cancel.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				cancelET.invokeListeners(getMe());
			}});
		
	}

	@Override
	protected Widget getSubWidget(String dwname) {
		if( dwname.equals("ok") ) return ok;
		else if( dwname.equals("cancel") ) return cancel;
		else if( dwname.equals("text") ) return text;
		else return null; 
	}
	
	
	
	public void addOkListener(CustomEventListener listener){
		okET.addCustomEventListener(listener);
	}
	
	public void removeOkListener(CustomEventListener listener){
		okET.removeCustomEventListener(listener);
	}
	
	public void addCancelListener(CustomEventListener listener){
		cancelET.addCustomEventListener(listener);
	}
	
	public void removeCancelListener(CustomEventListener listener){
		cancelET.removeCustomEventListener(listener);
	}
	
}
