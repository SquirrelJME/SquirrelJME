// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

public class NetworkInterface
{
	public NetworkInterface()
	{
		throw Debugging.todo();
	}
	
	public void connect(AccessPoint __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	public boolean disconnect(AccessPoint __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	public boolean disconnectAll()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	public AccessPoint[] getConnectedAccessPoints()
	{
		throw Debugging.todo();
	}
	
	public String getName()
	{
		throw Debugging.todo();
	}
	
	public String getType()
	{
		throw Debugging.todo();
	}
	
	public boolean isRoaming()
	{
		throw Debugging.todo();
	}
	
	public boolean supportsConcurrentAccessPoints()
	{
		throw Debugging.todo();
	}
	
	public static NetworkInterface[] getNetworkInterfaces(String __a)
	{
		throw Debugging.todo();
	}
	
	public static String[] getNetworkTypes()
	{
		throw Debugging.todo();
	}
}


