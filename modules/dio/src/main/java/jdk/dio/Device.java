// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.Channel;

@SuppressWarnings("DuplicateThrows")
@Api
public interface Device<P extends Device<? super P>>
	extends Channel
{
	@Api
	int BIG_ENDIAN =
		1;
	
	@Api
	int LITTLE_ENDIAN =
		0;
	
	@Api
	int MIXED_ENDIAN =
		2;
	
	@Api
	ByteOrder getByteOrder()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	<U extends P> DeviceDescriptor<U> getDescriptor();
	
	@Api
	void tryLock(int __a)
		throws UnavailableDeviceException, ClosedDeviceException, 
			IOException;
	
	@Api
	void unlock()
		throws IOException;
}


