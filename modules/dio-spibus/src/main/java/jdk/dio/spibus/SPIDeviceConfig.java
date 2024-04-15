// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spibus;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

@Api
public final class SPIDeviceConfig
	implements DeviceConfig<SPIDevice>, DeviceConfig.HardwareAddressing
{
	@Api
	public static final int CS_ACTIVE_HIGH =
		0;
	
	@Api
	public static final int CS_ACTIVE_LOW =
		1;
	
	@Api
	public static final int CS_NOT_CONTROLLED =
		2;
	
	@ApiDefinedDeprecated
	@Api
	public SPIDeviceConfig(int __a, int __b, int __c, int __d, int __e, int 
		__f)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public SPIDeviceConfig(int __a, int __b, int __c, int __d, int __e, int 
		__f, int __g)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public SPIDeviceConfig(String __a, int __b, int __c, int __d, int __e, 
		int __f)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public SPIDeviceConfig(String __a, int __b, int __c, int __d, int __e, 
		int __f, int __g)
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
	public int getBitOrdering()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getCSActiveLevel()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getClockFrequency()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getClockMode()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getControllerName()
	{
		throw Debugging.todo();
	}
	
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
	
	@Api
	public int getWordLength()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int serialize(OutputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static SPIDeviceConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static final class Builder
	{
		@Api
		public Builder()
		{
			throw Debugging.todo();
		}
		
		@Api
		public SPIDeviceConfig build()
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setAddress(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setBitOrdering(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setCSActiveLevel(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setClockFrequency(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setClockMode(int __a)
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
		
		@Api
		public Builder setWordLength(int __a)
		{
			throw Debugging.todo();
		}
	}
}


