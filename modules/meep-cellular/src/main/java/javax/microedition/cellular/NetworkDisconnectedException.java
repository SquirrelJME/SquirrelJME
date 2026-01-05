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

@Api
public class NetworkDisconnectedException
	extends Exception
{
	@Api
	public NetworkDisconnectedException()
	{
	}
	
	@Api
	public NetworkDisconnectedException(String __m)
	{
		super(__m);
	}
}
