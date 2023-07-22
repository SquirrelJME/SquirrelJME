// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.security.Permission;
import java.security.PermissionCollection;

@Api
public abstract class DevicePermission
	extends Permission
{
	@Api
	public static final String OPEN =
		"open";
	
	@Api
	public static final String POWER_MANAGE =
		"powermanage";
	
	@Api
	public DevicePermission(String __a)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	@Api
	public DevicePermission(String __a, String __b)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getActions()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean implies(Permission __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public PermissionCollection newPermissionCollection()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}


