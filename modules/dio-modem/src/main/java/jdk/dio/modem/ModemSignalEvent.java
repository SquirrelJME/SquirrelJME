// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.modem;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.Device;
import jdk.dio.DeviceEvent;

public class ModemSignalEvent<P extends Device<? super P>>
	extends DeviceEvent<P>
{
	protected int signalID;
	
	protected boolean signalState;
	
	public ModemSignalEvent(P __a, int __b, boolean __c)
	{
		throw Debugging.todo();
	}
	
	public ModemSignalEvent(P __a, int __b, boolean __c, long __d, int __e)
	{
		throw Debugging.todo();
	}
	
	public int getSignalID()
	{
		throw Debugging.todo();
	}
	
	public boolean getSignalState()
	{
		throw Debugging.todo();
	}
}


