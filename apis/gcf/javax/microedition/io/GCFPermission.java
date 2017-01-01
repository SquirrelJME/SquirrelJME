// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.security.Permission;
import java.security.PermissionCollection;

public abstract class GCFPermission
	extends Permission
{
	public GCFPermission(String __a)
	{
		super((String)null);
		throw new Error("TODO");
	}
	
	public String getProtocol()
	{
		throw new Error("TODO");
	}
	
	public String getURI()
	{
		throw new Error("TODO");
	}
	
	@Override
	public PermissionCollection newPermissionCollection()
	{
		throw new Error("TODO");
	}
}


