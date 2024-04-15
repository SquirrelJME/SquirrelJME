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
import java.util.EventListener;

@Api
public interface RegistrationListener<P extends Device<? super P>>
	extends EventListener
{
	@Api
	void deviceRegistered(RegistrationEvent<P> __a);
	
	@Api
	void deviceUnregistered(RegistrationEvent<P> __a);
}


