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


public abstract class DeviceEvent<P extends Device<? super P>>
{
	protected int count;
	
	protected P device;
	
	protected long lastTimeStamp;
	
	protected int lastTimeStampMicros;
	
	protected long timeStamp;
	
	protected int timeStampMicros;
	
	public DeviceEvent()
	{
		super();
		throw new Error("TODO");
	}
	
	public final int getCount()
	{
		throw new Error("TODO");
	}
	
	public final P getDevice()
	{
		throw new Error("TODO");
	}
	
	public final long getLastTimeStamp()
	{
		throw new Error("TODO");
	}
	
	public final int getLastTimeStampMicros()
	{
		throw new Error("TODO");
	}
	
	public final long getTimeStamp()
	{
		throw new Error("TODO");
	}
	
	public final int getTimeStampMicros()
	{
		throw new Error("TODO");
	}
}


