// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class ResourceOperatorManager
{
	@Api
	public static final int MELODY_RESOURCE = 0;
	
	@Api
	public static final int IMAGE_RESOURCE = 1;
	
	@Api
	public ResourceOperatorManager()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static ResourceOperator getResourceOperator(int var0)
	{
		throw Debugging.todo();
	}
}
