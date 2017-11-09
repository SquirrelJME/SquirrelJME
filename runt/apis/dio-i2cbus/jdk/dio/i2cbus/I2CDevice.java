// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.i2cbus;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface I2CDevice
	extends Device<I2CDevice>, ByteChannel, BufferAccess<ByteBuffer>
{
	public abstract I2CDevice.Bus getBus()
		throws IOException;
	
	public abstract int read()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int read(int __a, ByteBuffer __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int read(int __a, int __b, ByteBuffer __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int read(int __a, int __b, int __c, ByteBuffer __d)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract void write(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract int write(int __a, int __b, ByteBuffer __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public static interface Bus
	{
		public abstract I2CCombinedMessage createCombinedMessage();
	}
}


