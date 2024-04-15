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

public final class NativeThreadJoinner
{
	@Api
	public static NativeThreadJoinner createInstance()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void destroyInstance(NativeThreadJoinner var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void reset()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getId()
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized int getCause()
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized int getOption()
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized void join()
		throws InterruptedException
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized void join(long var1)
		throws InterruptedException
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean causeContains(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized void joinNotify(int var1, int var2, int var3)
	{
		throw Debugging.todo();
	}
}
