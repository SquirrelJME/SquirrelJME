// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.pwm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;
import jdk.dio.gpio.GPIOPin;
import jdk.dio.gpio.GPIOPinConfig;

public final class PWMChannelConfig
	implements DeviceConfig<PWMChannel>, DeviceConfig.HardwareAddressing
{
	public static final int ALIGN_CENTER =
		0;
	
	public static final int ALIGN_LEFT =
		1;
	
	public static final int ALIGN_RIGHT =
		2;
	
	public static final int IDLE_STATE_HIGH =
		0;
	
	public static final int IDLE_STATE_LOW =
		1;
	
	@Deprecated
	public PWMChannelConfig(int __a, int __b, int __c, int __d, int __e)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public PWMChannelConfig(int __a, int __b, int __c, int __d, int __e, 
		GPIOPinConfig __f)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public PWMChannelConfig(String __a, int __b, int __c, int __d, int __e)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public PWMChannelConfig(String __a, int __b, int __c, int __d, int __e, 
		GPIOPinConfig __f)
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
	
	public int getIdleState()
	{
		throw new Error("TODO");
	}
	
	@Deprecated
	public GPIOPin getOutput()
	{
		throw new Error("TODO");
	}
	
	public int getOutputBufferSize()
	{
		throw new Error("TODO");
	}
	
	public GPIOPinConfig getOutputConfig()
	{
		throw new Error("TODO");
	}
	
	public int getPulseAlignment()
	{
		throw new Error("TODO");
	}
	
	public int getPulsePeriod()
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
	
	public static PWMChannelConfig deserialize(InputStream __a)
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
		
		public PWMChannelConfig build()
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
		
		public Builder setIdleState(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setOutputConfig(GPIOPinConfig __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setPulseAlignment(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setPulsePeriod(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setScaleFactor(double __a)
		{
			throw new Error("TODO");
		}
	}
}


