// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public final class Debug
{
	@Api
	public static final boolean WATCH_TIME = false;
	
	@Api
	public Debug()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void trace(Object var0, String var1)
	{
		throw Debugging.todo();
	}
}
