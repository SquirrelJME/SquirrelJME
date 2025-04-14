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
import javax.microedition.io.Connection;

@Api
public class LocalDevice
{
	@Api
	public String getBluetoothAddress()
	{
		throw Debugging.todo();
	}
	
	@Api
	public DeviceClass getDeviceClass()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDiscoverable()
	{
		throw Debugging.todo();
	}
	
	@Api
	public DiscoveryAgent getDiscoveryAgent()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getFriendlyName()
	{
		throw Debugging.todo();
	}
	
	@Api
	public ServiceRecord getRecord(Connection __connection)
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public boolean setDiscoverable(int __i)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings({"unused", "RedundantThrows"})
	public void updateRecord(ServiceRecord __serviceRecord)
		throws ServiceRegistrationException
	{
		throw Debugging.todo();
	}
	
	@Api
	@SuppressWarnings("RedundantThrows")
	public static LocalDevice getLocalDevice()
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String getProperty(String __s)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static boolean isPowerOn()
	{
		throw Debugging.todo();
	}
}
