/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.presentation.ui.actions;

import org.eclipse.actf.visualization.presentation.RoomPlugin;
import org.eclipse.actf.visualization.presentation.ui.internal.PartControlRoom;
import org.eclipse.actf.visualization.presentation.util.ParamRoom;
import org.eclipse.jface.action.Action;



public class MiddleRoomSimulateAction extends Action {

    public MiddleRoomSimulateAction() {
        setToolTipText(RoomPlugin.getResourceString("RoomView.Simulate_Middle")); //$NON-NLS-1$
        setImageDescriptor(RoomPlugin.getImageDescriptor("icons/ButtonMiddle.gif")); //$NON-NLS-1$
        setText(RoomPlugin.getResourceString("RoomSimulationAction.Middle")); //$NON-NLS-1$
    }

    public void run() {
    	//TODO
        PartControlRoom.getDefaultInstance().getParamLowVision().setType(ParamRoom.ROOM_MIDDLE);
        PartControlRoom.getDefaultInstance().doSimulate();
    }

}
