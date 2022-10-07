// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.security.Permission;
import java.security.PermissionCollection;

public class DeviceMgmtPermission
	extends Permission
{
	public static final String OPEN =
		"open";
	
	public static final String REGISTER =
		"register";
	
	public static final String UNREGISTER =
		"unregister";
	
	public DeviceMgmtPermission(String __a, String __b)
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
}


