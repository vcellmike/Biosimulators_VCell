/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.rmi.event;

/**
 * Insert the type's description here.
 * Creation date: (11/12/2000 3:18:29 PM)
 * @author: IIM
 */
public interface MessageListener extends java.util.EventListener {
/**
 * 
 * @param event cbit.rmi.event.MessageEvent
 */
void messageEvent(cbit.rmi.event.MessageEvent event);
}
