// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;

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
		throw new Error("TODO");
	}
	
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public String getActions()
	{
		throw new Error("TODO");
	}
	
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public boolean implies(Permission __a)
	{
		throw new Error("TODO");
	}
	
	public PermissionCollection newPermissionCollection()
	{
		throw new Error("TODO");
	}
}


