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
import java.nio.channels.ByteChannel;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;

public interface SPIDevice
	extends Device<SPIDevice>, ByteChannel, BufferAccess<ByteBuffer>
{
	SPICompositeMessage createCompositeMessage();
	
	int getWordLength()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int read()
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Override
	int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	int read(int __a, ByteBuffer __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	@Override
	int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	void write(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	int writeAndRead(ByteBuffer __a, ByteBuffer __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	int writeAndRead(ByteBuffer __a, int __b, ByteBuffer __c)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	int writeAndRead(int __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
}


