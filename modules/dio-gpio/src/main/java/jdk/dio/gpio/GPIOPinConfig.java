// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

@Api
public final class GPIOPinConfig
	implements DeviceConfig<GPIOPin>, DeviceConfig.HardwareAddressing
{
	@Api
	public static final int DIR_BOTH_INIT_INPUT =
		2;
	
	@Api
	public static final int DIR_BOTH_INIT_OUTPUT =
		3;
	
	@Api
	public static final int DIR_INPUT_ONLY =
		0;
	
	@Api
	public static final int DIR_OUTPUT_ONLY =
		1;
	
	@Api
	public static final int MODE_INPUT_PULL_DOWN =
		2;
	
	@Api
	public static final int MODE_INPUT_PULL_UP =
		1;
	
	@Api
	public static final int MODE_OUTPUT_OPEN_DRAIN =
		8;
	
	@Api
	public static final int MODE_OUTPUT_PUSH_PULL =
		4;
	
	@Api
	public static final int TRIGGER_BOTH_EDGES =
		3;
	
	@Api
	public static final int TRIGGER_BOTH_LEVELS =
		6;
	
	@Api
	public static final int TRIGGER_FALLING_EDGE =
		1;
	
	@Api
	public static final int TRIGGER_HIGH_LEVEL =
		4;
	
	@Api
	public static final int TRIGGER_LOW_LEVEL =
		5;
	
	public static final int TRIGGER_NONE =
		0;
	
	public static final int TRIGGER_RISING_EDGE =
		2;
	
	@Api
	@ApiDefinedDeprecated
	public GPIOPinConfig(int __a, int __b, int __c, int __d, int __e, boolean
		__f)
	{
		throw Debugging.todo();
	}
	
	@Api
	@ApiDefinedDeprecated
	public GPIOPinConfig(String __a, int __b, int __c, int __d, int __e, 
		boolean __f)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
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
	public int getDirection()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDriveMode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean getInitValue()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getPinNumber()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getTrigger()
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
	public static GPIOPinConfig deserialize(InputStream __a)
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
		public GPIOPinConfig build()
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
		public Builder setDirection(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setDriveMode(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setInitValue(boolean __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setPinNumber(int __a)
		{
			throw Debugging.todo();
		}
		
		@Api
		public Builder setTrigger(int __a)
		{
			throw Debugging.todo();
		}
	}
}


