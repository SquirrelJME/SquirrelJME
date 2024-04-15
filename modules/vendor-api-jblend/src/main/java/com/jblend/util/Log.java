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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

@Api
public final class Log
{
	@Api
	public static final PrintStream out = Debugging.<PrintStream>todoObject();
	
	@Api
	public static final PrintStream err = Debugging.<PrintStream>todoObject();
	
	@Api
	public Log()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static final void out(String var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static final void err(String var0)
	{
		throw Debugging.todo();
	}
}

