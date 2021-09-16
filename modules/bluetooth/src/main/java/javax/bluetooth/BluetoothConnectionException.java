// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

public class BluetoothConnectionException
	extends IOException
{
	public static final int FAILED_NOINFO = 4;
	
	public static final int NO_RESOURCES = 3;
	
	public static final int SECURITY_BLOCK = 2;
	
	public static final int TIMEOUT = 5;
	
	public static final int UNACCEPTABLE_PARAMS = 6;
	
	public static final int UNKNOWN_PSM = 1;
	
	@SuppressWarnings("unused")
	public BluetoothConnectionException(int __i)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public BluetoothConnectionException(int __i, String __s)
	{
		throw Debugging.todo();
	}
	
	public int getStatus()
	{
		throw Debugging.todo();
	}
}
