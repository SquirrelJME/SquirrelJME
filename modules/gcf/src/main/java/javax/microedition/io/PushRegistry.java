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
public class PushRegistry
{
	@Api
	public PushRegistry()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String getFilter(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String getMIDlet(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String[] listConnections(boolean __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static long registerAlarm(String __a, long __b)
		throws ClassNotFoundException, ConnectionNotFoundException
	{
		if (false)
			throw new ClassNotFoundException();
		if (false)
			throw new ConnectionNotFoundException();
		throw Debugging.todo();
	}
	
	@Api
	public static void registerConnection(String __a, String __b, String __c)
		throws ClassNotFoundException, IOException
	{
		if (false)
			throw new ClassNotFoundException();
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static boolean unregisterConnection(String __a)
	{
		throw Debugging.todo();
	}
}


