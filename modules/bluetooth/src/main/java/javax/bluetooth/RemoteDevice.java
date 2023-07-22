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
import javax.microedition.io.Connection;

@Api
public class RemoteDevice
{
	@Api
	protected RemoteDevice(String __s)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings("RedundantThrows")
	public boolean authenticate()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public boolean authorize(Connection __connection)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public boolean encrypt(Connection __connection, boolean __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final String getBluetoothAddress()
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public String getFriendlyName(boolean __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isAuthenticated()
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public boolean isAuthorized(Connection __connection)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isEncrypted()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean isTrustedDevice()
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public static RemoteDevice getRemoteDevice(Connection __connection)
		throws IOException
	{
		throw Debugging.todo();
	}
}
