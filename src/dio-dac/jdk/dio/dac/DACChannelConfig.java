// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.dac;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class DACChannelConfig
	implements DeviceConfig<DACChannel>, DeviceConfig.HardwareAddressing
{
	@Deprecated
	public DACChannelConfig(int __a, int __b, int __c, int __d)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public DACChannelConfig(String __a, int __b, int __c, int __d)
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
	
	public int getOutputBufferSize()
	{
		throw new Error("TODO");
	}
	
	public int getResolution()
	{
		throw new Error("TODO");
	}
	
	public int getSamplingInterval()
	{
		throw new Error("TODO");
	}
	
	public double getScaleFactor()
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
	
	public static DACChannelConfig deserialize(InputStream __a)
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
		
		public DACChannelConfig build()
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
		
		public Builder setOutputBufferSize(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setResolution(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setSamplingInterval(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setScaleFactor(double __a)
		{
			throw new Error("TODO");
		}
	}
}


