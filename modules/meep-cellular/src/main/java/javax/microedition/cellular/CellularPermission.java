// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.cellular;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.security.BasicPermission;

@Api
public class CellularPermission
	extends BasicPermission
{
	@Api
	public CellularPermission(String __name)
	{
		super(__name);
	}
}
