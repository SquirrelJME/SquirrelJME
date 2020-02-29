// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spi;

import java.io.InputStream;
import java.io.IOException;
import jdk.dio.Device;
import jdk.dio.DeviceConfig;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.InvalidDeviceConfigException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedAccessModeException;

public interface DeviceProvider<P extends Device<? super P>>
{
	DeviceConfig<? super P> deserialize(InputStream __a)
		throws IOException;
	
	Class<? extends DeviceConfig<? super P>> getConfigType();
	
	Class<P> getType();
	
	boolean matches(String[] __a);
	
	AbstractDevice<? super P> open(DeviceConfig<? super P> __a, String[] __b,
		int __c)
		throws DeviceNotFoundException, UnavailableDeviceException, 
			InvalidDeviceConfigException, UnsupportedAccessModeException, 
			IOException;
}


