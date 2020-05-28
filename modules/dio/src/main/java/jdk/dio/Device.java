// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.Channel;

public interface Device<P extends Device<? super P>>
	extends Channel
{
	int BIG_ENDIAN =
		1;
	
	int LITTLE_ENDIAN =
		0;
	
	int MIXED_ENDIAN =
		2;
	
	@Override
	void close()
		throws IOException;
	
	ByteOrder getByteOrder()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	<U extends P> DeviceDescriptor<U> getDescriptor();
	
	@Override
	boolean isOpen();
	
	void tryLock(int __a)
		throws UnavailableDeviceException, ClosedDeviceException, 
			IOException;
	
	void unlock()
		throws IOException;
}


