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
public final class GPIOPortConfig
	implements DeviceConfig<GPIOPort>
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
	public GPIOPortConfig(int __a, int __b, GPIOPinConfig... __c)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getDirection()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getInitValue()
	{
		throw Debugging.todo();
	}
	
	@Api
	public GPIOPinConfig[] getPinConfigs()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
	@Api
	public GPIOPin[] getPins()
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
	public static GPIOPortConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
}


