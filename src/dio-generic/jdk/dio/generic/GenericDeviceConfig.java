// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class GenericDeviceConfig
	implements DeviceConfig<GenericDevice>, DeviceConfig.HardwareAddressing
{
	@Deprecated
	public GenericDeviceConfig(int __a, int __b)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public GenericDeviceConfig(String __a, int __b)
	{
		super();
		throw new Error("TODO");
	}
	
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public int getChannelNumber()
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
	
	public static GenericDeviceConfig deserialize(InputStream __a)
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
		
		public GenericDeviceConfig build()
		{
			throw new Error("TODO");
		}
		
		public Builder setChannelNumber(int __a)
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


