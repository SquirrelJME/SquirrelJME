// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.power;

import cc.squirreljme.runtime.cldc.annotation.Api;
import jdk.dio.Device;

@Api
public interface PowerSavingHandler
{
	@Api
	<P extends Device<? super P>> void handlePowerStateChange(P __a,
		PowerManaged.Group __b, int __c, int __d, long __e);
	
	@Api
	<P extends Device<? super P>> long
		handlePowerStateChangeRequest(P __a, PowerManaged.Group __b, int __c,
		int __d, long __e);
}


