// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.mmio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.nio.ByteBuffer;
import jdk.dio.ClosedDeviceException;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface RawBlock
	extends RawMemory
{
	@Api
	ByteBuffer asDirectBuffer()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
}

