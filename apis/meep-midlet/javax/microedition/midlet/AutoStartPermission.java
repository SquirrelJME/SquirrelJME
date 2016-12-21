// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import java.security.BasicPermission;
import java.security.Permission;

public final class AutoStartPermission
	extends BasicPermission
{
	public AutoStartPermission()
	{
		super(null);
		throw new Error("TODO");
	}

	public boolean implies(Permission __p)
	{
		throw new Error("TODO");
	}
}
