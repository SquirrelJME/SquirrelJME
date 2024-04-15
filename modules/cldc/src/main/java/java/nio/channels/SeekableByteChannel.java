// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.channels;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

@Api
public interface SeekableByteChannel
	extends ByteChannel
{
	@Api
	long position()
		throws IOException;
	
	@Api
	SeekableByteChannel position(long __a)
		throws IOException;
	
	@Api
	long size()
		throws IOException;
	
	@Api
	SeekableByteChannel truncate(long __a)
		throws IOException;
	
}


