// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class GenericDeviceConfig
	implements DeviceConfig<GenericDevice>, DeviceConfig.HardwareAddressing
{
	@Deprecated
	public GenericDeviceConfig(int __a, int __b)
	{
		super();
		throw new todo.TODO();
	}
	
	@Deprecated
	public GenericDeviceConfig(String __a, int __b)
	{
		super();
		throw new todo.TODO();
	}
	
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public int getChannelNumber()
	{
		throw new todo.TODO();
	}
	
	public String getControllerName()
	{
		throw new todo.TODO();
	}
	
	public int getControllerNumber()
	{
		throw new todo.TODO();
	}
	
	public int getInputBufferSize()
	{
		throw new todo.TODO();
	}
	
	public int getOutputBufferSize()
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public int serialize(OutputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static GenericDeviceConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static final class Builder
	{
		public Builder()
		{
			super();
			throw new todo.TODO();
		}
		
		public GenericDeviceConfig build()
		{
			throw new todo.TODO();
		}
		
		public Builder setChannelNumber(int __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setControllerName(String __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setControllerNumber(int __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setInputBufferSize(int __a)
		{
			throw new todo.TODO();
		}
		
		public Builder setOutputBufferSize(int __a)
		{
			throw new todo.TODO();
		}
	}
}


