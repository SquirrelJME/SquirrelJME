// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.gpio;

import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import jdk.dio.DeviceConfig;

public final class GPIOPortConfig
	implements DeviceConfig<GPIOPort>
{
	public static final int DIR_BOTH_INIT_INPUT =
		2;
	
	public static final int DIR_BOTH_INIT_OUTPUT =
		3;
	
	public static final int DIR_INPUT_ONLY =
		0;
	
	public static final int DIR_OUTPUT_ONLY =
		1;
	
	public GPIOPortConfig(int __a, int __b, GPIOPinConfig... __c)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	public int getDirection()
	{
		throw Debugging.todo();
	}
	
	public int getInitValue()
	{
		throw Debugging.todo();
	}
	
	public GPIOPinConfig[] getPinConfigs()
	{
		throw Debugging.todo();
	}
	
	@ApiDefinedDeprecated
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
	
	public static GPIOPortConfig deserialize(InputStream __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
}


