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

import org.dvijok.db.DB_Object;
import org.dvijok.widgets.Sub_Panels_Dwidget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class EditorCKEditor extends Sub_Panels_Dwidget implements Editor {

	private SimplePanel ed;
	private String edId;
	private JavaScriptObject editor;
	private String html = "1111";

	public EditorCKEditor(){
		super("tmpl/widgets/editor/editorckeditor.html");
	}

	@Override
	public String getHTML() {
		return getHTML(editor);
	}

	@Override
	public void setHTML(String html) {
		this.html = html;
		setHTML(editor, html);
	}
	
	public void destroy(){
		destroy(editor);
	}

	@Override
	protected void Before_Sub_Panels_Loading() {
		edId = "ed" + Random.nextInt();
		SimplePanel sp = new SimplePanel();
		ed = new SimplePanel();
		ed.getElement().setId(edId);
		sp.add(ed);
		org.dvijok.resources.Resources.getInstance().addToTmp(sp);
		editor = initEditor(edId, this);
		org.dvijok.resources.Resources.getInstance().removeFromTmp(sp);
	}
	
	private static native void destroy(JavaScriptObject editor)/*-{
		editor.destroy();
		editor=null;
	}-*/;
	
	private static native String getHTML(JavaScriptObject editor)/*-{
		return editor.getData();
	}-*/;
	
	private static native void setHTML(JavaScriptObject editor, String html)/*-{
		editor.insertHtml( html );
	}-*/;
	
	private static native JavaScriptObject initEditor(String edId, EditorCKEditor ed)/*-{
		var config = {};
		config.language = 'ru';
		var editor = $wnd.CKEDITOR.appendTo( edId, config );
		editor.on( 'instanceReady', function(){
			editor.insertHtml( ed.@org.dvijok.widgets.editor.EditorCKEditor::html );
		} );
		return editor;
	}-*/;	

	@Override
	protected Widget Gen_Sub_Widget(String dwname, ArrayList<DB_Object> params) {
		if( dwname.equals("editor") ){
			return ed;
		} else return null;
	}
	
}
