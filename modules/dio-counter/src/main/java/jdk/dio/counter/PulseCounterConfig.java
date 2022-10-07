// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	
	@ApiDefinedDeprecated
	public PulseCounterConfig(int __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public PulseCounterConfig(int __a, int __b, int __c, GPIOPinConfig __d)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public PulseCounterConfig(String __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public PulseCounterConfig(String __a, int __b, int __c, GPIOPinConfig __d
		)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	public int getChannelNumber()
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
	
	@ApiDefinedDeprecated
	public GPIOPin getSource()
	{
		throw Debugging.todo();
	}
	
	public GPIOPinConfig getSourceConfig()
	{
		throw Debugging.todo();
	}
	
	public int getType()
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
	
	public static PulseCounterConfig deserialize(InputStream __a)
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
		
		public PulseCounterConfig build()
		{
			throw Debugging.todo();
		}
		
		public Builder setChannelNumber(int __a)
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
		
		public Builder setSourceConfig(GPIOPinConfig __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setType(int __a)
		{
			throw Debugging.todo();
		}
	}
}


