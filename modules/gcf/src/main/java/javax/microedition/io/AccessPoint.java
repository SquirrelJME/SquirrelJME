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
public class AccessPoint
{
	@Api
	public AccessPoint()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void addListener(AccessPointListener __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean connect()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public boolean disconnect()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public String getName()
	{
		throw Debugging.todo();
	}
	
	@Api
	public NetworkInterface getNetworkInterface()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getProperty(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public String[] getPropertyNames()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String[] getPropertyValues(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getTimeout()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getType()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isConnected()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isRemovable()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void removeListener(AccessPointListener __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setTimeout(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static AccessPoint[] getAccessPoints(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static AccessPoint of(String __a, ConnectionOption... __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static boolean remove(AccessPoint __a)
	{
		throw Debugging.todo();
	}
}


