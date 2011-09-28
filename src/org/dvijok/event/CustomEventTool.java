//SAAS

package org.dvijok.event;

import java.util.ArrayList;

/**
 *
 * @author Pechenko Anton aka parilo, forpost78 aaaaaat gmail doooooot com
 *         (C) Copyright by Pechenko Anton, created 09.12.2010
 */
public class CustomEventTool {

    private ArrayList<CustomEventListener> ls;
    private Object source;
    private CustomEvent ev;

    public CustomEventTool(){
        ls = new ArrayList<CustomEventListener>();
    }

    public void addCustomEventListener(CustomEventListener l){
        ls.add(l);
    }

    public void removeCustomEventListener(CustomEventListener l){
        ls.remove(l);
    }

    public void invokeListeners(){
        for(int i=0; i<ls.size(); i++){
            ls.get(i).customEventOccurred(ev);
        }
    }

    public void invokeListeners(Object source){
        setSource(source);
        invokeListeners();
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
        ev = new CustomEvent(source);
    }
    
    public void removeAllListeners(){
    	ls.clear();
    }

}
