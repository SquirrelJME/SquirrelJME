// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spi;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;
import java.io.InputStream;
import jdk.dio.Device;
import jdk.dio.DeviceConfig;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.InvalidDeviceConfigException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedAccessModeException;

@Api
public interface DeviceProvider<P extends Device<? super P>>
{
	@Api
	DeviceConfig<? super P> deserialize(InputStream __a)
		throws IOException;
	
	@Api
	Class<? extends DeviceConfig<? super P>> getConfigType();
	
	@Api
	Class<P> getType();
	
	@Api
	boolean matches(String[] __a);
	
	@SuppressWarnings("DuplicateThrows")
	@Api
	AbstractDevice<? super P> open(DeviceConfig<? super P> __a, String[] __b,
		int __c)
		throws DeviceNotFoundException, UnavailableDeviceException, 
			InvalidDeviceConfigException, UnsupportedAccessModeException, 
			IOException;
}


