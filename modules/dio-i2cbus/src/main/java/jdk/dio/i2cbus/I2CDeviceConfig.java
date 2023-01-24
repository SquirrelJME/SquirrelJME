// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.i2cbus;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class I2CDeviceConfig
	implements DeviceConfig<I2CDevice>, DeviceConfig.HardwareAddressing
{
	@Api
	public static final int ADDR_SIZE_10 =
		10;
	
	@Api
	public static final int ADDR_SIZE_7 =
		7;
	
	@Api
	@ApiDefinedDeprecated
	public I2CDeviceConfig(int __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Api
	@ApiDefinedDeprecated
	public I2CDeviceConfig(String __a, int __b, int __c, int __d)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getAddress()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getAddressSize()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getClockFrequency()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public String getControllerName()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public int getControllerNumber()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getInputBufferSize()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getOutputBufferSize()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public int serialize(OutputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static I2CDeviceConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	public static final class Builder
	{
		@Api
		public Builder()
		{
			throw Debugging.todo();
		}
		
		@Api
		public I2CDeviceConfig build()
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setAddress(int __a, int __b)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setClockFrequency(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setControllerName(String __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setControllerNumber(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setInputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setOutputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
	}
}


