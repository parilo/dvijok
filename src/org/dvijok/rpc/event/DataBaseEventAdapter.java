/**
 *
 * @author Dmitry Zubanov, 2011
 */

package org.dvijok.rpc.event;


public abstract class DataBaseEventAdapter extends DataBaseEventListener{
    
//    public void objectAdded(DataBaseEvent evt){}
//    
//    public void objectModifyed(DataBaseEvent evt){}
//    
//    public void objectDeleted(DataBaseEvent evt){}
//    
//    public void allEvent(DataBaseEvent evt){}
	
	public void eventReceived(DataBaseEvent evt){}
    
}
