// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.spibus;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class SPIDeviceConfig
	implements DeviceConfig<SPIDevice>, DeviceConfig.HardwareAddressing
{
	public static final int CS_ACTIVE_HIGH =
		0;
	
	public static final int CS_ACTIVE_LOW =
		1;
	
	public static final int CS_NOT_CONTROLLED =
		2;
	
	@ApiDefinedDeprecated
	public SPIDeviceConfig(int __a, int __b, int __c, int __d, int __e, int 
		__f)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public SPIDeviceConfig(int __a, int __b, int __c, int __d, int __e, int 
		__f, int __g)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public SPIDeviceConfig(String __a, int __b, int __c, int __d, int __e, 
		int __f)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
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
	
	public int getAddress()
	{
		throw Debugging.todo();
	}
	
	public int getBitOrdering()
	{
		throw Debugging.todo();
	}
	
	public int getCSActiveLevel()
	{
		throw Debugging.todo();
	}
	
	public int getClockFrequency()
	{
		throw Debugging.todo();
	}
	
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
	
	public int getInputBufferSize()
	{
		throw Debugging.todo();
	}
	
	public int getOutputBufferSize()
	{
		throw Debugging.todo();
	}
	
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
	
	public static SPIDeviceConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	public static final class Builder
	{
		public Builder()
		{
			throw Debugging.todo();
		}
		
		public SPIDeviceConfig build()
		{
			throw Debugging.todo();
		}
		
		public Builder setAddress(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setBitOrdering(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setCSActiveLevel(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setClockFrequency(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setClockMode(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setControllerName(String __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setControllerNumber(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setInputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setWordLength(int __a)
		{
			throw Debugging.todo();
		}
	}
}


