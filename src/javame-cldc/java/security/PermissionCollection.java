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

