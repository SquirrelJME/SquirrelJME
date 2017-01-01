// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.mmio;

import java.io.IOException;
import java.nio.ByteBuffer;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

public interface MMIODevice
	extends Device<MMIODevice>
{
	public abstract RawBlock getAsRawBlock()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract RawBlock getBlock(String __n)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract void setMMIOEventListener(int __evid, int __cdx,
		ByteBuffer __cbuf, MMIOEventListener __el)
		throws ClosedDeviceException, IOException;
	
	public abstract void setMMIOEventListener(int __evid,
		MMIOEventListener __el)
		throws ClosedDeviceException, IOException;
	
	public abstract void setMMIOEventListener(int __evid, String __cn,
		MMIOEventListener __el)
		throws ClosedDeviceException, IOException;
}

