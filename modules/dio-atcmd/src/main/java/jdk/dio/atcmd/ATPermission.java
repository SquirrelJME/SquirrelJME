// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.atcmd;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import jdk.dio.DevicePermission;

@Api
public class ATPermission
	extends DevicePermission
{
	@Api
	public static final String DATA =
		"data";
	
	@Api
	public ATPermission(String __a)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	@Api
	public ATPermission(String __a, String __b)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	@Override
	public String getActions()
	{
		throw Debugging.todo();
	}
}


