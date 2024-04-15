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

@Api
public class DiscoveryAgent
{
	@Api
	public static final int CACHED = 0;
	
	@Api
	public static final int GIAC = 10390323;
	
	@Api
	public static final int LIAC = 10390272;
	
	@Api
	public static final int NOT_DISCOVERABLE = 0;
	
	@Api
	public static final int PREKNOWN = 1;
	
	@Api
	DiscoveryAgent()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean cancelInquiry(DiscoveryListener __discoveryListener)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean cancelServiceSearch(int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public RemoteDevice[] retrieveDevices(int __i)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int searchServices(int[] __ints, UUID[] __uuids,
		RemoteDevice __remoteDevice, DiscoveryListener __discoveryListener)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@Api
	public String selectService(UUID __uuid, int __i, boolean __b)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean startInquiry(int __i,
	 DiscoveryListener __discoveryListener)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
}
