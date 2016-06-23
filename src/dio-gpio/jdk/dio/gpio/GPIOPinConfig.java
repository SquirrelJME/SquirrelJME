// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class GPIOPinConfig
	implements DeviceConfig<GPIOPin>, DeviceConfig.HardwareAddressing
{
	public static final int DIR_BOTH_INIT_INPUT =
		2;
	
	public static final int DIR_BOTH_INIT_OUTPUT =
		3;
	
	public static final int DIR_INPUT_ONLY =
		0;
	
	public static final int DIR_OUTPUT_ONLY =
		1;
	
	public static final int MODE_INPUT_PULL_DOWN =
		2;
	
	public static final int MODE_INPUT_PULL_UP =
		1;
	
	public static final int MODE_OUTPUT_OPEN_DRAIN =
		8;
	
	public static final int MODE_OUTPUT_PUSH_PULL =
		4;
	
	public static final int TRIGGER_BOTH_EDGES =
		3;
	
	public static final int TRIGGER_BOTH_LEVELS =
		6;
	
	public static final int TRIGGER_FALLING_EDGE =
		1;
	
	public static final int TRIGGER_HIGH_LEVEL =
		4;
	
	public static final int TRIGGER_LOW_LEVEL =
		5;
	
	public static final int TRIGGER_NONE =
		0;
	
	public static final int TRIGGER_RISING_EDGE =
		2;
	
	@Deprecated
	public GPIOPinConfig(int __a, int __b, int __c, int __d, int __e, boolean
		__f)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public GPIOPinConfig(String __a, int __b, int __c, int __d, int __e, 
		boolean __f)
	{
		super();
		throw new Error("TODO");
	}
	
	public boolean equals(Object __a)
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
	
	public int getDirection()
	{
		throw new Error("TODO");
	}
	
	public int getDriveMode()
	{
		throw new Error("TODO");
	}
	
	public boolean getInitValue()
	{
		throw new Error("TODO");
	}
	
	public int getPinNumber()
	{
		throw new Error("TODO");
	}
	
	public int getTrigger()
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
	
	public static GPIOPinConfig deserialize(InputStream __a)
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
		
		public GPIOPinConfig build()
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
		
		public Builder setDirection(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setDriveMode(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setInitValue(boolean __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setPinNumber(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setTrigger(int __a)
		{
			throw new Error("TODO");
		}
	}
}


