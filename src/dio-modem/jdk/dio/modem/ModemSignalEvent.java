// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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
		throw new Error("TODO");
	}
	
	public ModemSignalEvent(P __a, int __b, boolean __c, long __d, int __e)
	{
		super();
		throw new Error("TODO");
	}
	
	public int getSignalID()
	{
		throw new Error("TODO");
	}
	
	public boolean getSignalState()
	{
		throw new Error("TODO");
	}
}


