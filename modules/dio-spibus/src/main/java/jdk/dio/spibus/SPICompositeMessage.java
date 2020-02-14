// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spibus;

import java.io.IOException;
import java.nio.ByteBuffer;
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

public interface SPICompositeMessage
{
	public abstract SPICompositeMessage appendDelay(int __a)
		throws IOException, ClosedDeviceException;
	
	public abstract SPICompositeMessage appendRead(ByteBuffer __a)
		throws IOException, ClosedDeviceException;
	
	public abstract SPICompositeMessage appendRead(int __a, ByteBuffer __b)
		throws IOException, ClosedDeviceException;
	
	public abstract SPICompositeMessage appendWrite(ByteBuffer __a)
		throws IOException, ClosedDeviceException;
	
	public abstract SPICompositeMessage appendWriteAndRead(ByteBuffer __a, 
		ByteBuffer __b)
		throws IOException, ClosedDeviceException;
	
	public abstract SPICompositeMessage appendWriteAndRead(ByteBuffer __a, 
		int __b, ByteBuffer __c)
		throws IOException, ClosedDeviceException;
	
	public abstract SPIDevice getTargetedDevice();
	
	public abstract int[] transfer()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


