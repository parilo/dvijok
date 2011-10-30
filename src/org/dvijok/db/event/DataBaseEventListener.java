/**
 *
 * @author Dmitry Zubanov, 2011
 */

package org.dvijok.db.event;


public interface DataBaseEventListener {

    public void objectAdded(DataBaseEvent evt);
    
    public void objectModifyed(DataBaseEvent evt);
    
    public void objectDeleted(DataBaseEvent evt);
    
    public void allEvent(DataBaseEvent evt);
    
}
