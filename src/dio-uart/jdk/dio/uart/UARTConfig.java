// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio.uart;

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
	
	@Deprecated
	public UARTConfig(int __a, int __b, int __c, int __d, int __e, int __f, 
		int __g)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public UARTConfig(int __a, int __b, int __c, int __d, int __e, int __f, 
		int __g, int __h, int __i)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public UARTConfig(String __a, int __b, int __c, int __d, int __e, int __f
		, int __g)
	{
		super();
		throw new Error("TODO");
	}
	
	@Deprecated
	public UARTConfig(String __a, int __b, int __c, int __d, int __e, int __f
		, int __g, int __h, int __i)
	{
		super();
		throw new Error("TODO");
	}
	
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public int getBaudRate()
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
	
	public int getDataBits()
	{
		throw new Error("TODO");
	}
	
	public int getFlowControlMode()
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
	
	public int getParity()
	{
		throw new Error("TODO");
	}
	
	public int getStopBits()
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
	
	public static UARTConfig deserialize(InputStream __a)
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
		
		public UARTConfig build()
		{
			throw new Error("TODO");
		}
		
		public Builder setBaudRate(int __a)
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
		
		public Builder setDataBits(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setFlowControlMode(int __a)
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
		
		public Builder setParity(int __a)
		{
			throw new Error("TODO");
		}
		
		public Builder setStopBits(int __a)
		{
			throw new Error("TODO");
		}
	}
}


