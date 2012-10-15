//    dvijok - cms written in gwt
//    Copyright (C) 2010-2012  Pechenko Anton Vladimirovich aka Parilo
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

package org.dvijok.controls;

import org.dvijok.event.CustomEventListener;
import org.dvijok.event.CustomEventTool;
import org.dvijok.resources.Resources;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;

public class FileUploader extends FormPanel {

	private DivPanel panel;
	private FileUpload upload;
	private Hidden sid;
	private Hidden fileid;
	private Hidden maxFileSize;
	
	private CustomEventTool uploaded;
	
	public FileUploader(){
		
		uploaded = new CustomEventTool();

		this.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.setMethod(FormPanel.METHOD_POST);
		this.setAction("dvrpc/filesupload.php");
		
		panel = new DivPanel();
		
		upload = new FileUpload();
		upload.setName("fileupload");

		sid = new Hidden();
		sid.setName("sid");
		sid.setValue(Resources.getInstance().db.getSid());

		fileid = new Hidden();
		fileid.setName("fileid");

		maxFileSize = new Hidden();
		maxFileSize.setName("MAX_FILE_SIZE");
		maxFileSize.setValue("104857600");
		
		panel.addWidget(maxFileSize);
		panel.addWidget(upload);
		panel.addWidget(sid);
		panel.addWidget(fileid);
		
		this.add(panel);
		
		addSubmitCompleteHandler(new SubmitCompleteHandler(){
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploaded.invokeListeners();
			}});
		
	}
	
	public void setInline(){
		getElement().getStyle().setDisplay(Display.INLINE);
		panel.getElement().getStyle().setDisplay(Display.INLINE);
	}
	
	public void setTitle(String title){
		upload.setTitle(title);
	}
	
	public void setFileId(String id){
		fileid.setValue(id);
	}
	
	public String getFileId(){
		return fileid.getValue();
	}
	
	public void sendFile(){
		submit();
	}

	public void setEnabled(boolean enabled){
		upload.setEnabled(enabled);
	}
	
	public String getFilename(){
		return upload.getFilename();
	}
	
	public void addUploadedListener(CustomEventListener listener){
		uploaded.addCustomEventListener(listener);
	}
	
	public void removeUploadedListener(CustomEventListener listener){
		uploaded.removeCustomEventListener(listener);
	}
	
}
