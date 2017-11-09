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
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

public interface I2CCombinedMessage
{
	public abstract I2CCombinedMessage appendRead(I2CDevice __a, ByteBuffer 
		__b)
		throws IOException, ClosedDeviceException;
	
	public abstract I2CCombinedMessage appendRead(I2CDevice __a, int __b, 
		ByteBuffer __c)
		throws IOException, ClosedDeviceException;
	
	public abstract I2CCombinedMessage appendWrite(I2CDevice __a, ByteBuffer 
		__b)
		throws IOException, ClosedDeviceException;
	
	public abstract int[] transfer()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


