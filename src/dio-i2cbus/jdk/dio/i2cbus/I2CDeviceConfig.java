// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.i2cbus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class I2CDeviceConfig
	implements DeviceConfig<I2CDevice>, DeviceConfig.HardwareAddressing
{
	public static final int ADDR_SIZE_10 =
		10;
	
	public static final int ADDR_SIZE_7 =
		7;
	
	@Deprecated
	public I2CDeviceConfig(int __a, int __b, int __c, int __d)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public I2CDeviceConfig(String __a, int __b, int __c, int __d)
	{
		super();
		throw new Error("TODO");
	}
	
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public int getAddress()
	{
		throw new Error("TODO");
	}
	
	public int getAddressSize()
	{
		throw new Error("TODO");
	}
	
	public int getClockFrequency()
	{
		throw new Error("TODO");
	}
	
	public String getControllerName()
	{
		throw new Error("TODO");
	}
	
	public int getControllerNumber()
	{
		throw new Error("TODO");
	}
	
	public int getInputBufferSize()
	{
		throw new Error("TODO");
	}
	
	public int getOutputBufferSize()
	{
		throw new Error("TODO");
	}
	
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public int serialize(OutputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static I2CDeviceConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static final class Builder
	{
		public Builder()
		{
			super();
			throw new Error("TODO");
		}
		
		public I2CDeviceConfig build()
		{
			throw new Error("TODO");
		}
		
		public Builder setAddress(int __a, int __b)
		{
			throw new Error("TODO");
		}
		
		public Builder setClockFrequency(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setControllerName(String __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setControllerNumber(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setInputBufferSize(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw new Error("TODO");
		}
	}
}


