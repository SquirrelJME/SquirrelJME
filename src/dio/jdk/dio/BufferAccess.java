// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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


