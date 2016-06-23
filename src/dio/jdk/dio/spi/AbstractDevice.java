// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.spi;

import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.DeviceDescriptor;
import jdk.dio.UnavailableDeviceException;

public abstract class AbstractDevice<P extends Device<? super P>>
	implements Device<P>
{
	public AbstractDevice()
	{
		super();
		throw new Error("TODO");
	}
	
	public final <U extends P> DeviceDescriptor<U> getDescriptor()
	{
		throw new Error("TODO");
	}
	
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
		throw new Error("TODO");
	}
	
	public void unlock()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
}


