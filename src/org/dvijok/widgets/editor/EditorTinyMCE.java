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

import org.dvijok.widgets.Dwidget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class EditorTinyMCE extends Dwidget implements Editor {

	private TextArea ed;
	private String edId;
	private JavaScriptObject editor;
	private String html;
	private boolean inited;

	public EditorTinyMCE(){
		super("tmpl/widgets/editor/editortinymce.html");
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public String getHTML() {
		return "";
//		return getHTML(editor);
	}

	@Override
	public void setHTML(String htmlstr) {
//		this.html = htmlstr;
//		if( inited ){
//			setHTML(editor, htmlstr);
//		}
	}

	@Override
	public void setHeight(String height) {
	}
	
//	public void destroy(){
//		destroy(editor);
//	}

	@Override
	protected void initInternals() {
		ed = new TextArea();
//		ed.addStyleName("tinymce_ed");
		ed.getElement().setId("tinymce_ed");
//		initEditor();
	}
	
//	private static native void destroy(JavaScriptObject editor)/*-{
//		editor.destroy();
//		editor=null;
//	}-*/;
//	
//	private static native String getHTML(JavaScriptObject editor)/*-{
//		return editor.getData();
//	}-*/;
//	
//	private static native void setHTML(JavaScriptObject editor, String html)/*-{
//		editor.insertHtml( html );
//	}-*/;
	
	@Override
	public void initEditor(){
		initEditorJS();
	}
	
//	private static native JavaScriptObject initEditor(String edId, EditorTinyMCE ed)/*-{
	private static native void initEditorJS()/*-{
		alert($wnd.tinyMCE.execCommand("mceAddControl", true, "tinymce_ed")); 
//		$wnd.tinyMCE.init({
//		        mode : "specific_textareas",
//		        editor_selector : "tinymce_ed"
//		});
	}-*/;	

	@Override
	protected Widget getSubWidget(String name) {
		if( name.equals("editor") ){
			return ed;
		} else return null;
	}
	
}
