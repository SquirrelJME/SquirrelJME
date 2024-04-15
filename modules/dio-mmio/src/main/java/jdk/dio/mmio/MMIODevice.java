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
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface MMIODevice
	extends Device<MMIODevice>
{
	@Api
	RawBlock getAsRawBlock()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	RawBlock getBlock(String __n)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Api
	void setMMIOEventListener(int __evid, int __cdx, ByteBuffer __cbuf,
		MMIOEventListener __el)
		throws ClosedDeviceException, IOException;
	
	@Api
	void setMMIOEventListener(int __evid, MMIOEventListener __el)
		throws ClosedDeviceException, IOException;
	
	@Api
	void setMMIOEventListener(int __evid, String __cn, MMIOEventListener __el)
		throws ClosedDeviceException, IOException;
}

