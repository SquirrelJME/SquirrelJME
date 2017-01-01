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

import java.util.Enumeration;

public abstract class PermissionCollection
{
	public PermissionCollection()
	{
		super();
		throw new Error("TODO");
	}
	
	public abstract void add(Permission __a);
	
	public abstract Enumeration<Permission> elements();
	
	public abstract boolean implies(Permission __a);
	
	public boolean isReadOnly()
	{
		throw new Error("TODO");
	}
	
	public void setReadOnly()
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

