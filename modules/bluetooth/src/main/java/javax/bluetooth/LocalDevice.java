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
import javax.microedition.io.Connection;

public class LocalDevice
{
	public String getBluetoothAddress()
	{
		throw Debugging.todo();
	}
	
	public DeviceClass getDeviceClass()
	{
		throw Debugging.todo();
	}
	
	public int getDiscoverable()
	{
		throw Debugging.todo();
	}
	
	public DiscoveryAgent getDiscoveryAgent()
	{
		throw Debugging.todo();
	}
	
	public String getFriendlyName()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public ServiceRecord getRecord(Connection __connection)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"unused", "RedundantThrows"})
	public boolean setDiscoverable(int __i)
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"unused", "RedundantThrows"})
	public void updateRecord(ServiceRecord __serviceRecord)
		throws ServiceRegistrationException
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("RedundantThrows")
	public static LocalDevice getLocalDevice()
		throws BluetoothStateException
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("unused")
	public static String getProperty(String __s)
	{
		throw Debugging.todo();
	}
	
	public static boolean isPowerOn()
	{
		throw Debugging.todo();
	}
}
