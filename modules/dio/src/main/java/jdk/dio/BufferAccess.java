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
import java.nio.Buffer;
import java.nio.ByteBuffer;

@SuppressWarnings("DuplicateThrows")
@Api
public interface BufferAccess<B extends Buffer>
{
	@Api
	B getInputBuffer()
		throws ClosedDeviceException, IOException;
	
	@Api
	B getOutputBuffer()
		throws ClosedDeviceException, IOException;
	
	@Api
	B prepareBuffer(ByteBuffer __a, int __b)
		throws IOException, ClosedDeviceException;
}


