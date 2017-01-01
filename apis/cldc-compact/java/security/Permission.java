// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

public abstract class Permission
{
	public Permission(String __a)
	{
		super();
		throw new Error("TODO");
	}
	
	@Override
	public abstract boolean equals(Object __a);
	
	public abstract String getActions();
	
	@Override
	public abstract int hashCode();
	
	public abstract boolean implies(Permission __a);
	
	public final String getName()
	{
		throw new Error("TODO");
	}
	
	public PermissionCollection newPermissionCollection()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

