// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Enumeration;

public abstract class PermissionCollection
{
	@Api
	public PermissionCollection()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract void add(Permission __a);
	
	@Api
	public abstract Enumeration<Permission> elements();
	
	@Api
	public abstract boolean implies(Permission __a);
	
	@Api
	public boolean isReadOnly()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setReadOnly()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}

