// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import jdk.dio.ClosedDeviceException;
import jdk.dio.Device;
import jdk.dio.UnavailableDeviceException;

@SuppressWarnings("DuplicateThrows")
@Api
public interface GenericDevice
	extends Device<GenericDevice>
{
	@Api
	<T> T getControl(GenericDeviceControl<T> __a)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	<T> void setControl(GenericDeviceControl<T> __a, T __b)
		throws IOException, UnavailableDeviceException, 
			ClosedDeviceException;
	
	@Api
	void setEventListener(int __a, GenericEventListener __b)
		throws IOException, ClosedDeviceException;
}


