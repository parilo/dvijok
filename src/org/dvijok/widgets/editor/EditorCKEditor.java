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
	private String height;
	private String externalCss;
	private String templatesFile;
	private CustomEventTool ready;
	private boolean isReady;

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
		setHTML(editor, htmlstr);
	}

	@Override
	public void setHeight(String height) {
		this.height = height;
		setHeight(editor, height);
	}
	
	public void addReadyListener(CustomEventListener listener){
		ready.addCustomEventListener(listener);
	}
	
	public void removeReadyListener(CustomEventListener listener){
		ready.removeCustomEventListener(listener);
	}
	
	private void invokeReady(){
		setHeight(height);
		ready.invokeListeners();
		isReady = true;
	}
	
	public void destroy(){
		isReady = false;
		destroy(editor);
	}

	public boolean isReady() {
		return isReady;
	}

	public String getExternalCss() {
		return externalCss;
	}

	public void setExternalCss(String externalCss) {
		this.externalCss = externalCss;
	}

	public String getTemplatesFile() {
		return templatesFile;
	}

	public void setTemplatesFile(String templatesFile) {
		this.templatesFile = templatesFile;
	}

	@Override
	protected void beforeSubPanelsLoading() {
		isReady = false;
		ready = new CustomEventTool();
		editor = null;
		html = "";
		externalCss = "";
		templatesFile = "";
		height = "400";
		edId = "ed" + Random.nextInt();
		ed = new SimplePanel();
		ed.getElement().setId(edId);
		
		ed.addAttachHandler(new Handler(){
			@Override
			public void onAttachOrDetach(AttachEvent event) {
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
		editor.setData( html );
	}-*/;
	
	private static native void setHeight(JavaScriptObject editor, String hsize)/*-{
		editor.resize('100%', hsize);
	}-*/;
	
	private static native JavaScriptObject initEditorJS(String edId, EditorCKEditor ed)/*-{
		var config = {};
//		config.extraPlugins = 'autogrow';
		config.language = 'ru';
		config.ignoreEmptyParackgraph = true;
		if( ed.@org.dvijok.widgets.editor.EditorCKEditor::externalCss != "" ) config.contentsCss = ed.@org.dvijok.widgets.editor.EditorCKEditor::externalCss;
		if( ed.@org.dvijok.widgets.editor.EditorCKEditor::templatesFile != "" ) config.templates_files = [ ed.@org.dvijok.widgets.editor.EditorCKEditor::templatesFile ];
		config.templates_replaceContent = false;
//		config.autoGrow_onStartup = true;
//		config.removePlugins = 'resize';
		
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
