// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.modem;

import jdk.dio.Device;
import jdk.dio.DeviceEvent;

public class ModemSignalEvent<P extends Device<? super P>>
	extends DeviceEvent<P>
{
	protected int signalID;
	
	protected boolean signalState;
	
	public ModemSignalEvent(P __a, int __b, boolean __c)
	{
		super();
		throw new todo.TODO();
	}
	
	public ModemSignalEvent(P __a, int __b, boolean __c, long __d, int __e)
	{
		super();
		throw new todo.TODO();
	}
	
	public int getSignalID()
	{
		throw new todo.TODO();
	}
	
	public boolean getSignalState()
	{
		throw new todo.TODO();
	}
}


