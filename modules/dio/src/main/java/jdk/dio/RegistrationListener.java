// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import java.util.EventListener;

public interface RegistrationListener<P extends Device<? super P>>
	extends EventListener
{
	void deviceRegistered(RegistrationEvent<P> __a);
	
	void deviceUnregistered(RegistrationEvent<P> __a);
}


