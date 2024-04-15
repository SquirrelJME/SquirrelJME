// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.generic;


import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class GenericDeviceControl<T>
{
	@Api
	public GenericDeviceControl(int __a, Class<T> __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getID()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Class<T> getType()
	{
		throw Debugging.todo();
	}
}


