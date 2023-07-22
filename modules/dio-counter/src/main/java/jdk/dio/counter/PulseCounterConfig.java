// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.counter;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;

@Api
public final class PulseCounterConfig
	implements DeviceConfig<PulseCounter>, DeviceConfig.HardwareAddressing
{
	@Api
	public static final int TYPE_FALLING_EDGE_ONLY =
		0;
	
	@Api
	public static final int TYPE_NEGATIVE_PULSE =
		3;
	
	@Api
	public static final int TYPE_POSITIVE_PULSE =
		2;
	
	@Api
	public static final int TYPE_RISING_EDGE_ONLY =
		1;
	
	@ApiDefinedDeprecated
	@Api
	public PulseCounterConfig(int __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public PulseCounterConfig(int __a, int __b, int __c, GPIOPinConfig __d)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public PulseCounterConfig(String __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
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
	
	@Api
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
	@Api
	public GPIOPin getSource()
	{
		throw Debugging.todo();
	}
	
	@Api
	public GPIOPinConfig getSourceConfig()
	{
		throw Debugging.todo();
	}
	
	@Api
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
	
	@Api
	public static PulseCounterConfig deserialize(InputStream __a)
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
		public PulseCounterConfig build()
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setChannelNumber(int __a)
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
		public Builder setSourceConfig(GPIOPinConfig __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setType(int __a)
		{
			throw Debugging.todo();
		}
	}
}


