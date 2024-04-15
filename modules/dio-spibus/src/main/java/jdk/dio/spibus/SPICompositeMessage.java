// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spibus;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.ByteBuffer;
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface SPICompositeMessage
{
	@Api
	SPICompositeMessage appendDelay(int __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	SPICompositeMessage appendRead(ByteBuffer __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	SPICompositeMessage appendRead(int __a, ByteBuffer __b)
		throws IOException, ClosedDeviceException;
	
	@Api
	SPICompositeMessage appendWrite(ByteBuffer __a)
		throws IOException, ClosedDeviceException;
	
	@Api
	SPICompositeMessage appendWriteAndRead(ByteBuffer __a, ByteBuffer __b)
		throws IOException, ClosedDeviceException;
	
	@Api
	SPICompositeMessage appendWriteAndRead(ByteBuffer __a, int __b,
		ByteBuffer __c)
		throws IOException, ClosedDeviceException;
	
	@Api
	SPIDevice getTargetedDevice();
	
	@Api
	int[] transfer()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


