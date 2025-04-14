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
import java.nio.ByteBuffer;

@Api
public interface WritableByteChannel
	extends Channel
{
	@Api
	int write(ByteBuffer __a)
		throws IOException;
}

