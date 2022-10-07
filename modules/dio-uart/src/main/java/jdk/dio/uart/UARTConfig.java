// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class UARTConfig
	implements DeviceConfig<UART>, DeviceConfig.HardwareAddressing
{
	public static final int DATABITS_5 =
		5;
	
	public static final int DATABITS_6 =
		6;
	
	public static final int DATABITS_7 =
		7;
	
	public static final int DATABITS_8 =
		8;
	
	public static final int DATABITS_9 =
		9;
	
	public static final int FLOWCONTROL_NONE =
		0;
	
	public static final int FLOWCONTROL_RTSCTS_IN =
		1;
	
	public static final int FLOWCONTROL_RTSCTS_OUT =
		2;
	
	public static final int FLOWCONTROL_XONXOFF_IN =
		4;
	
	public static final int FLOWCONTROL_XONXOFF_OUT =
		8;
	
	public static final int PARITY_EVEN =
		2;
	
	public static final int PARITY_MARK =
		3;
	
	public static final int PARITY_NONE =
		0;
	
	public static final int PARITY_ODD =
		1;
	
	public static final int PARITY_SPACE =
		4;
	
	public static final int STOPBITS_1 =
		1;
	
	public static final int STOPBITS_1_5 =
		2;
	
	public static final int STOPBITS_2 =
		3;
	
	@ApiDefinedDeprecated
	public UARTConfig(int __a, int __b, int __c, int __d, int __e, int __f, 
		int __g)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public UARTConfig(int __a, int __b, int __c, int __d, int __e, int __f, 
		int __g, int __h, int __i)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public UARTConfig(String __a, int __b, int __c, int __d, int __e, int __f
		, int __g)
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	public UARTConfig(String __a, int __b, int __c, int __d, int __e, int __f
		, int __g, int __h, int __i)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	public int getBaudRate()
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
	
	public int getDataBits()
	{
		throw Debugging.todo();
	}
	
	public int getFlowControlMode()
	{
		throw Debugging.todo();
	}
	
	public int getInputBufferSize()
	{
		throw Debugging.todo();
	}
	
	public int getOutputBufferSize()
	{
		throw Debugging.todo();
	}
	
	public int getParity()
	{
		throw Debugging.todo();
	}
	
	public int getStopBits()
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
	
	public static UARTConfig deserialize(InputStream __a)
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
		
		public UARTConfig build()
		{
			throw Debugging.todo();
		}
		
		public Builder setBaudRate(int __a)
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
		
		public Builder setDataBits(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setFlowControlMode(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setInputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setParity(int __a)
		{
			throw Debugging.todo();
		}
		
		public Builder setStopBits(int __a)
		{
			throw Debugging.todo();
		}
	}
}


