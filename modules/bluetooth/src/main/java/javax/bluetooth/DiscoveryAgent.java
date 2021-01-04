// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.bluetooth;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public class DiscoveryAgent
{
	public static final int CACHED = 0;
	
	public static final int GIAC = 10390323;
	
	public static final int LIAC = 10390272;
	
	public static final int NOT_DISCOVERABLE = 0;
	
	public static final int PREKNOWN = 1;
	
	@SuppressWarnings("unused")
	DiscoveryAgent()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public boolean cancelInquiry(DiscoveryListener __discoveryListener)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public boolean cancelServiceSearch(int __i)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public RemoteDevice[] retrieveDevices(int __i)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"unused", "RedundantThrows"})
	public int searchServices(int[] __ints, UUID[] __uuids,
		RemoteDevice __remoteDevice, DiscoveryListener __discoveryListener)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"unused", "RedundantThrows"})
	public String selectService(UUID __uuid, int __i, boolean __b)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"unused", "RedundantThrows"})
	public boolean startInquiry(int __i,
	 DiscoveryListener __discoveryListener)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
}
