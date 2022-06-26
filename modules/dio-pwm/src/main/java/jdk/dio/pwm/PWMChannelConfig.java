// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.pwm;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	
	@ApiDefinedDeprecated
	public PWMChannelConfig(int __a, int __b, int __c, int __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public PWMChannelConfig(int __a, int __b, int __c, int __d, int __e, 
		GPIOPinConfig __f)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public PWMChannelConfig(String __a, int __b, int __c, int __d, int __e)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public PWMChannelConfig(String __a, int __b, int __c, int __d, int __e, 
		GPIOPinConfig __f)
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
	
	public int getIdleState()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public GPIOPin getOutput()
	{
		throw Debugging.todo();
	}
	
	public int getOutputBufferSize()
	{
		throw Debugging.todo();
	}
	
	public GPIOPinConfig getOutputConfig()
	{
		throw Debugging.todo();
	}
	
	public int getPulseAlignment()
	{
		throw Debugging.todo();
	}
	
	public int getPulsePeriod()
	{
		throw Debugging.todo();
	}
	
	public double getScaleFactor()
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
	
	public static PWMChannelConfig deserialize(InputStream __a)
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
		
		public PWMChannelConfig build()
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
		
		public Builder setIdleState(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setOutputConfig(GPIOPinConfig __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setPulseAlignment(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setPulsePeriod(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setScaleFactor(double __a)
		{
			throw Debugging.todo();
		}
	}
}


