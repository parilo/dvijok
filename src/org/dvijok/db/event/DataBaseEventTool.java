//    dvijok - cms written in gwt
//    Copyright (C) 2010-2011  Pechenko Anton Vladimirovich aka Parilo
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

package org.dvijok.db.event;

import java.util.ArrayList;

public class DataBaseEventTool {

    private ArrayList<DataBaseEventListener> ls;
    private DataBaseEvent ev;

    public DataBaseEventTool(){
        ls = new ArrayList<DataBaseEventListener>();
    }

    public void addDataBaseListener(DataBaseEventListener l){
        ls.add(l);
    }

    public void removeDataBaseListener(DataBaseEventListener l){
        ls.remove(l);
    }

    public void invokeAddListeners(DataBaseEvent ev){
        for(int i=0; i<ls.size(); i++){
        	DataBaseEventListener l = ls.get(i);
            l.objectAdded(ev);
            l.allEvent(ev);
        }
    }

    public void invokeModifyListeners(DataBaseEvent ev){
        for(int i=0; i<ls.size(); i++){
        	DataBaseEventListener l = ls.get(i);
            l.objectModifyed(ev);
            l.allEvent(ev);
        }
    }

    public void invokeDelListeners(DataBaseEvent ev){
        for(int i=0; i<ls.size(); i++){
        	DataBaseEventListener l = ls.get(i);
            l.objectDeleted(ev);
            l.allEvent(ev);
        }
    }
    
    public void removeAllListeners(){
    	ls.clear();
    }

}
