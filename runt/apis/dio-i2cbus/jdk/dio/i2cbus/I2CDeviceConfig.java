// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
		throw new todo.TODO();
	}
	
	@Deprecated
	public I2CDeviceConfig(String __a, int __b, int __c, int __d)
	{
		super();
		throw new todo.TODO();
	}
	
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public int getAddress()
	{
		throw new todo.TODO();
	}
	
	public int getAddressSize()
	{
		throw new todo.TODO();
	}
	
	public int getClockFrequency()
	{
		throw new todo.TODO();
	}
	
	public String getControllerName()
	{
		throw new todo.TODO();
	}
	
	public int getControllerNumber()
	{
		throw new todo.TODO();
	}
	
	public int getInputBufferSize()
	{
		throw new todo.TODO();
	}
	
	public int getOutputBufferSize()
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public int serialize(OutputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static I2CDeviceConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static final class Builder
	{
		public Builder()
		{
			super();
			throw new todo.TODO();
		}
		
		public I2CDeviceConfig build()
		{
			throw new todo.TODO();
		}
		
		public Builder setAddress(int __a, int __b)
		{
			throw new todo.TODO();
		}
		
		public Builder setClockFrequency(int __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setControllerName(String __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setControllerNumber(int __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setInputBufferSize(int __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw new todo.TODO();
		}
	}
}


