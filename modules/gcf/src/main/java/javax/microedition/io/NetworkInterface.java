// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

@Api
public class NetworkInterface
{
	@Api
	public NetworkInterface()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void connect(AccessPoint __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public boolean disconnect(AccessPoint __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public boolean disconnectAll()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public AccessPoint[] getConnectedAccessPoints()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getName()
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
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean supportsConcurrentAccessPoints()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static NetworkInterface[] getNetworkInterfaces(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String[] getNetworkTypes()
	{
		throw Debugging.todo();
	}
}


