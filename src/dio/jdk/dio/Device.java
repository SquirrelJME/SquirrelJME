// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.channels.Channel;

public interface Device<P extends Device<? super P>>
	extends Channel
{
	public static final int BIG_ENDIAN =
		1;
	
	public static final int LITTLE_ENDIAN =
		0;
	
	public static final int MIXED_ENDIAN =
		2;
	
	public abstract void close()
		throws IOException;
	
	public abstract ByteOrder getByteOrder()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	public abstract <U extends P> DeviceDescriptor<U> getDescriptor();
	
	public abstract boolean isOpen();
	
	public abstract void tryLock(int __a)
		throws UnavailableDeviceException, ClosedDeviceException, 
			IOException;
	
	public abstract void unlock()
		throws IOException;
}


