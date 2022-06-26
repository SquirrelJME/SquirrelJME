// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.security.Permission;
import java.security.PermissionCollection;

public abstract class GCFPermission
	extends Permission
{
	public GCFPermission(String __a)
	{
		super((String)null);
		throw Debugging.todo();
	}
	
	public String getProtocol()
	{
		throw Debugging.todo();
	}
	
	public String getURI()
	{
		throw Debugging.todo();
	}
	
	@Override
	public PermissionCollection newPermissionCollection()
	{
		throw Debugging.todo();
	}
}


