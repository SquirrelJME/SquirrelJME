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
import java.nio.Buffer;
import java.nio.ByteBuffer;

public interface BufferAccess<B extends Buffer>
{
	public abstract B getInputBuffer()
		throws ClosedDeviceException, IOException;
	
	public abstract B getOutputBuffer()
		throws ClosedDeviceException, IOException;
	
	public abstract B prepareBuffer(ByteBuffer __a, int __b)
		throws IOException, ClosedDeviceException;
}


