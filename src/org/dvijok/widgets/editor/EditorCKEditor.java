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

package org.dvijok.widgets.editor;

import java.util.ArrayList;

import org.dvijok.db.DBObject;
import org.dvijok.event.CustomEvent;
import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.lib.Lib;
import org.dvijok.widgets.SubPanelsDwidget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class EditorCKEditor extends SubPanelsDwidget implements Editor {

	private SimplePanel ed;
	private String edId;
	private JavaScriptObject editor;
	private String html;
//	private boolean inited;
	private CustomEventTool ready;

	public EditorCKEditor(){
		super("tmpl/widgets/editor/editorckeditor.html");
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public String getHTML() {
		return getHTML(editor);
	}

	@Override
	public void setHTML(String htmlstr) {
//		Lib.alert("set: "+htmlstr);
//		if( editor == null ) this.html = htmlstr;
//		else
		setHTML(editor, htmlstr);
	}
	
	public void addReadyListener(CustomEventListener listener){
		ready.addCustomEventListener(listener);
	}
	
	public void removeReadyListener(CustomEventListener listener){
		ready.removeCustomEventListener(listener);
	}
	
	private void invokeReady(){
//		Lib.alert("ready");
		ready.invokeListeners();
	}
	
	public void destroy(){
		destroy(editor);
	}

	@Override
	protected void beforeSubPanelsLoading() {
//		inited = false;
		ready = new CustomEventTool();
		editor = null;
		html = "11111";
		edId = "ed" + Random.nextInt();
		ed = new SimplePanel();
		ed.getElement().setId(edId);
		
		ed.addAttachHandler(new Handler(){
			@Override
			public void onAttachOrDetach(AttachEvent event) {
//				Lib.alert("attached: "+event.isAttached());
				if( event.isAttached() ){
					initEditor();
				} else {
					destroy();
				}
			}});

	}
	
	@Override
	protected void afterLoading() {
	}

	@Override
	public void initEditor() {
		editor = initEditorJS(edId, this);
	}

	private static native void destroy(JavaScriptObject editor)/*-{
		editor.destroy();
		editor=null;
	}-*/;
	
	private static native String getHTML(JavaScriptObject editor)/*-{
		return editor.getData();
	}-*/;
	
	private static native void setHTML(JavaScriptObject editor, String html)/*-{
//		editor.insertHtml( html );
		editor.setData( html );
	}-*/;
	
	private static native JavaScriptObject initEditorJS(String edId, EditorCKEditor ed)/*-{
		var config = {};
		config.language = 'ru';
		var editor = $wnd.CKEDITOR.appendTo( edId, config );
		editor.on( 'instanceReady', function(){
//			editor.insertHtml( ed.@org.dvijok.widgets.editor.EditorCKEditor::html );
			ed.@org.dvijok.widgets.editor.EditorCKEditor::invokeReady()();
		} );
		return editor;
	}-*/;	
	
	@Override
	protected Widget genSubWidget(String dwname, ArrayList<DBObject> params) {
		if( dwname.equals("editor") ){
			return ed;
		} else return null;
	}
	
}
