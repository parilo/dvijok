/**
 *
 * @author Dmitry Zubanov, 2011
 */

package org.dvijok.db.event;

import org.dvijok.db.DBArray;
import org.dvijok.db.DBObject;


public abstract class DataBaseEventListener {

	private DBArray tagsArray;
	
    public DBArray getTagsArray() {
		return tagsArray;
	}

	public void setTagsArray(DBArray tagsArray) {
		this.tagsArray = tagsArray;
	}

	public void setTagsArray(String tagsArrayString) {
		tagsArray = new DBArray();
		String[] strarr = tagsArrayString.split(" ");
		for(int i=0; i<strarr.length; i++){
			tagsArray.add(strarr[i]);
		}
	}

//    public abstract void objectAdded(DataBaseEvent evt);
//    
//    public abstract void objectModifyed(DataBaseEvent evt);
//    
//    public abstract void objectDeleted(DataBaseEvent evt);
//    
//    public abstract void allEvent(DataBaseEvent evt);
	
	public abstract void eventReceived(DataBaseEvent evt);
    
}
