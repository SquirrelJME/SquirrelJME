// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;

public final class PulseCounterConfig
	implements DeviceConfig<PulseCounter>, DeviceConfig.HardwareAddressing
{
	public static final int TYPE_FALLING_EDGE_ONLY =
		0;
	
	public static final int TYPE_NEGATIVE_PULSE =
		3;
	
	public static final int TYPE_POSITIVE_PULSE =
		2;
	
	public static final int TYPE_RISING_EDGE_ONLY =
		1;
	
	@Deprecated
	public PulseCounterConfig(int __a, int __b, int __c)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public PulseCounterConfig(int __a, int __b, int __c, GPIOPinConfig __d)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public PulseCounterConfig(String __a, int __b, int __c)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public PulseCounterConfig(String __a, int __b, int __c, GPIOPinConfig __d
		)
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
	
	@Deprecated
	public GPIOPin getSource()
	{
		throw new Error("TODO");
	}
	
	public GPIOPinConfig getSourceConfig()
	{
		throw new Error("TODO");
	}
	
	public int getType()
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
	
	public static PulseCounterConfig deserialize(InputStream __a)
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
		
		public PulseCounterConfig build()
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
		
		public Builder setSourceConfig(GPIOPinConfig __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setType(int __a)
		{
			throw new Error("TODO");
		}
	}
}


