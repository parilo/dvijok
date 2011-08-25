package org.dvijok.event;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.EventListener;

/**
 *
 * @author developer
 */
// Declare the listener class. It must extend EventListener.
// A class must implement this interface to get MyEvents.
public interface CustomEventListener extends EventListener {

    public void customEventOccurred(CustomEvent evt);
}

