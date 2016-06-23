// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.spibus;

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
	
	@Deprecated
	public SPIDeviceConfig(int __a, int __b, int __c, int __d, int __e, int 
		__f)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public SPIDeviceConfig(int __a, int __b, int __c, int __d, int __e, int 
		__f, int __g)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public SPIDeviceConfig(String __a, int __b, int __c, int __d, int __e, 
		int __f)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public SPIDeviceConfig(String __a, int __b, int __c, int __d, int __e, 
		int __f, int __g)
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
	
	public int getBitOrdering()
	{
		throw new Error("TODO");
	}
	
	public int getCSActiveLevel()
	{
		throw new Error("TODO");
	}
	
	public int getClockFrequency()
	{
		throw new Error("TODO");
	}
	
	public int getClockMode()
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
	
	public int getWordLength()
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
	
	public static SPIDeviceConfig deserialize(InputStream __a)
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
		
		public SPIDeviceConfig build()
		{
			throw new Error("TODO");
		}
		
		public Builder setAddress(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setBitOrdering(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setCSActiveLevel(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setClockFrequency(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setClockMode(int __a)
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
		
		public Builder setWordLength(int __a)
		{
			throw new Error("TODO");
		}
	}
}


