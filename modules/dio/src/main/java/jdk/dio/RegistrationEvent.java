// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.EventObject;

public class RegistrationEvent<P extends Device<? super P>>
	extends EventObject
{
	public RegistrationEvent(String __a, DeviceDescriptor<P> __b)
	{
		super((Object)null);
		throw Debugging.todo();
	}
	
	public RegistrationEvent(DeviceDescriptor<P> __a)
	{
		super((Object)null);
		throw Debugging.todo();
	}
	
	public DeviceDescriptor<P> getDescriptor()
	{
		throw Debugging.todo();
	}
	
	public String getInitiator()
	{
		throw Debugging.todo();
	}
}


