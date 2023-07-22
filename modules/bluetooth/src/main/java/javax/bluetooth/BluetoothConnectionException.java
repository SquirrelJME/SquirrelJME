// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

@Api
public class BluetoothConnectionException
	extends IOException
{
	@Api
	public static final int FAILED_NOINFO = 4;
	
	@Api
	public static final int NO_RESOURCES = 3;
	
	@Api
	public static final int SECURITY_BLOCK = 2;
	
	@Api
	public static final int TIMEOUT = 5;
	
	@Api
	public static final int UNACCEPTABLE_PARAMS = 6;
	
	@Api
	public static final int UNKNOWN_PSM = 1;
	
	@Api
	public BluetoothConnectionException(int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public BluetoothConnectionException(int __i, String __s)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getStatus()
	{
		throw Debugging.todo();
	}
}
