// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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

