// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.util.EventListener;

public interface RegistrationListener<P extends Device<? super P>>
	extends EventListener
{
	public abstract void deviceRegistered(RegistrationEvent<P> __a);
	
	public abstract void deviceUnregistered(RegistrationEvent<P> __a);
}


