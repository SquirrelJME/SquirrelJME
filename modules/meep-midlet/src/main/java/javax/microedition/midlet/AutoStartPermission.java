// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.security.BasicPermission;
import java.security.Permission;

public final class AutoStartPermission
	extends BasicPermission
{
	public AutoStartPermission()
	{
		super(null);
		throw Debugging.todo();
	}

	@Override
	public boolean implies(Permission __p)
	{
		throw Debugging.todo();
	}
}
