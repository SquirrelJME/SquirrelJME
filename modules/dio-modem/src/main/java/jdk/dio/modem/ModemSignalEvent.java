// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.modem;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.Device;
import jdk.dio.DeviceEvent;

@Api
public class ModemSignalEvent<P extends Device<? super P>>
	extends DeviceEvent<P>
{
	@Api
	protected int signalID;
	
	@Api
	protected boolean signalState;
	
	@Api
	public ModemSignalEvent(P __a, int __b, boolean __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ModemSignalEvent(P __a, int __b, boolean __c, long __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getSignalID()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getSignalState()
	{
		throw Debugging.todo();
	}
}


