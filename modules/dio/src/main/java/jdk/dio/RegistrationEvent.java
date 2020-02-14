// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.util.EventObject;

public class RegistrationEvent<P extends Device<? super P>>
	extends EventObject
{
	public RegistrationEvent(String __a, DeviceDescriptor<P> __b)
	{
		super((Object)null);
		throw new todo.TODO();
	}
	
	public RegistrationEvent(DeviceDescriptor<P> __a)
	{
		super((Object)null);
		throw new todo.TODO();
	}
	
	public DeviceDescriptor<P> getDescriptor()
	{
		throw new todo.TODO();
	}
	
	public String getInitiator()
	{
		throw new todo.TODO();
	}
}


