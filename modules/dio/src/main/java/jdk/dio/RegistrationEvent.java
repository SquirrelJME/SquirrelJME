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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.EventObject;

@Api
public class RegistrationEvent<P extends Device<? super P>>
	extends EventObject
{
	@Api
	public RegistrationEvent(String __a, DeviceDescriptor<P> __b)
	{
		super((Object)null);
		throw Debugging.todo();
	}
	
	@Api
	public RegistrationEvent(DeviceDescriptor<P> __a)
	{
		super((Object)null);
		throw Debugging.todo();
	}
	
	@Api
	public DeviceDescriptor<P> getDescriptor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getInitiator()
	{
		throw Debugging.todo();
	}
}


