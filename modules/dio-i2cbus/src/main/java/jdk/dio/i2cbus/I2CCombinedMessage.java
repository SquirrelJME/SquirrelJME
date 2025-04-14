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
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface I2CCombinedMessage
{
	@Api
	I2CCombinedMessage appendRead(I2CDevice __a, ByteBuffer __b)
		throws IOException, ClosedDeviceException;
	
	@Api
	I2CCombinedMessage appendRead(I2CDevice __a, int __b, ByteBuffer __c)
		throws IOException, ClosedDeviceException;
	
	@Api
	I2CCombinedMessage appendWrite(I2CDevice __a, ByteBuffer __b)
		throws IOException, ClosedDeviceException;
	
	@Api
	int[] transfer()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


