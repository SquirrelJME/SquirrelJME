// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public interface BufferAccess<B extends Buffer>
{
	B getInputBuffer()
		throws ClosedDeviceException, IOException;
	
	B getOutputBuffer()
		throws ClosedDeviceException, IOException;
	
	B prepareBuffer(ByteBuffer __a, int __b)
		throws IOException, ClosedDeviceException;
}


