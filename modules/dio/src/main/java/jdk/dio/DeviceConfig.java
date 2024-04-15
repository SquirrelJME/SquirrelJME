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
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import java.io.IOException;
import java.io.OutputStream;

@Api
public interface DeviceConfig<P extends Device<? super P>>
{
	@ApiDefinedDeprecated
	@Api
	int DEFAULT =
		-1;
	
	@Api
	int UNASSIGNED =
		-1;
	
	@Api
	int serialize(OutputStream __a)
		throws IOException;
	
	@Api
	interface HardwareAddressing
	{
		@Api
		String getControllerName();
		
		@Api
		int getControllerNumber();
	}
}


