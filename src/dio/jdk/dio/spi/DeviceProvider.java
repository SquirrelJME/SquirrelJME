// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.spi;

import java.io.IOException;
import java.io.InputStream;
import jdk.dio.Device;
import jdk.dio.DeviceConfig;
import jdk.dio.DeviceNotFoundException;
import jdk.dio.InvalidDeviceConfigException;
import jdk.dio.UnavailableDeviceException;
import jdk.dio.UnsupportedAccessModeException;

public interface DeviceProvider<P extends Device<? super P>>
{
	public abstract DeviceConfig<? super P> deserialize(InputStream __a)
		throws IOException;
	
	public abstract Class<? extends DeviceConfig<? super P>> getConfigType();
	
	public abstract Class<P> getType();
	
	public abstract boolean matches(String[] __a);
	
	public abstract AbstractDevice<? super P> open(DeviceConfig<? super P> 
		__a, String[] __b, int __c)
		throws DeviceNotFoundException, UnavailableDeviceException, 
			InvalidDeviceConfigException, UnsupportedAccessModeException, 
			IOException;
}


