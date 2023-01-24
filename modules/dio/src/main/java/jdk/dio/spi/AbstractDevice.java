// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spi;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.DeviceDescriptor;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public abstract class AbstractDevice<P extends Device<? super P>>
	implements Device<P>
{
	@Api
	public AbstractDevice()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public final <U extends P> DeviceDescriptor<U> getDescriptor()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void tryLock(int __a)
		throws UnavailableDeviceException, ClosedDeviceException, 
			IOException
	{
		if (false)
			throw new UnavailableDeviceException();
		if (false)
			throw new ClosedDeviceException();
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void unlock()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
}


