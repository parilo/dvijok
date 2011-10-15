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

package org.dvijok.event;

import java.util.ArrayList;

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
