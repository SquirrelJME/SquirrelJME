// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.i2cbus;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface I2CDevice
	extends Device<I2CDevice>, ByteChannel, BufferAccess<ByteBuffer>
{
	@Api
	I2CDevice.Bus getBus()
		throws IOException;
	
	@Api
	int read()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int read(int __a, ByteBuffer __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int read(int __a, int __b, ByteBuffer __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int read(int __a, int __b, int __c, ByteBuffer __d)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void write(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	int write(int __a, int __b, ByteBuffer __c)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	interface Bus
	{
		@Api
		I2CCombinedMessage createCombinedMessage();
	}
}


