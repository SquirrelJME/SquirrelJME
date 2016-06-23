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

import java.util.EventObject;

public class RegistrationEvent<P extends Device<? super P>>
	extends EventObject
{
	public RegistrationEvent(String __a, DeviceDescriptor<P> __b)
	{
		super((Object)null);
		throw new Error("TODO");
	}
	
	public RegistrationEvent(DeviceDescriptor<P> __a)
	{
		super((Object)null);
		throw new Error("TODO");
	}
	
	public DeviceDescriptor<P> getDescriptor()
	{
		throw new Error("TODO");
	}
	
	public String getInitiator()
	{
		throw new Error("TODO");
	}
}


