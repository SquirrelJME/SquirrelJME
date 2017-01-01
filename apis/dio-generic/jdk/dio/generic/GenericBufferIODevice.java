// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import jdk.dio.BufferAccess;
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedByteOrderException;

public interface GenericBufferIODevice
	extends GenericDevice, ByteChannel, BufferAccess<ByteBuffer>
{
	public abstract int read(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract int read(int __a, ByteBuffer __b)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
	
	public abstract int write(ByteBuffer __a)
		throws IOException, UnavailableDeviceException, 
			UnsupportedByteOrderException, ClosedDeviceException;
}


