// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.mmio;

public interface MMIODevice
	extends Device<MMIODevice>
{
	public abstract RawBlock getAsRawBlock()
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	public abstract RawBlock getBlock(String __n)
		throws ClosedDeviceException, IOException, UnavailableDeviceException;
	
	@Deprecated
	public abstract int getByteOrder()
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

