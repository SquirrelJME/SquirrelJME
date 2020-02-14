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

import java.util.EventListener;

public interface RegistrationListener<P extends Device<? super P>>
	extends EventListener
{
	public abstract void deviceRegistered(RegistrationEvent<P> __a);
	
	public abstract void deviceUnregistered(RegistrationEvent<P> __a);
}


