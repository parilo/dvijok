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

	private static final long serialVersionUID = 1L;

	public CustomEvent(Object source) {
        super(source);
    }
}
