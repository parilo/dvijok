package org.dvijok.event;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.EventObject;

/**
 *
 * @author developer
 */
public class CustomEvent extends EventObject {

    public CustomEvent(Object source) {
        super(source);
    }
}
