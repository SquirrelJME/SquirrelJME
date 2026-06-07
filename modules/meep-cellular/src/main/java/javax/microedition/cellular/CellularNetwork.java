// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.cellular;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.util.Map;
import javax.microedition.io.NetworkInterface;

@Api
public class CellularNetwork
{
	@Api
	public static final int NETWORK_AVAILABLE = 2;
	
	@Api
	public static final int NETWORK_FORBIDDEN = 4;
	
	@Api
	public static final int NETWORK_LIMITED = 5;
	
	@Api
	public static final int NETWORK_REGISTERED = 1;
	
	@Api
	public static final int NETWORK_UNKNOWN = 3;
	
	@Api
	public boolean deRegister()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public Map<String, String> getCellProperties()
		throws NetworkDisconnectedException, SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getName()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public NetworkInterface getNetworkInterface()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Map<String, String> getProperties()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getSignalStrength()
		throws NetworkDisconnectedException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getStatus()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Subscriber getSubscriber()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getType()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isRoaming()
		throws NetworkDisconnectedException
	{
		throw Debugging.todo();
	}
	
	@Api
	public CellularNetwork register(Subscriber __sub)
		throws IOException, SecurityException, UnsupportedOperationException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void addListener(CellularNetworkListener __listener)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static CellularNetwork[] getAvailableNetworks()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static CellularNetwork getByName(String __name)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static CellularNetwork getByNetworkInterface(NetworkInterface __ni)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static CellularNetwork[] getRegisteredNetworks()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void removeListener(CellularNetworkListener __listener)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
