// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
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


