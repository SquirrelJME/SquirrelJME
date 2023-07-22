// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@SuppressWarnings("NewMethodNamingConvention")
public class JoclEventDispatcher
	implements EventDispatcherInterface
{
	
	@Override
	public void dispatch(int var1, int var2, int var3, int var4)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setPhraseEventDispatcherInterface(
		JoclEventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setMediaEventDispatcherInterface(
		JoclEventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setPlatformEventDispatcherInterface(
		JoclEventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setVendorEventDispatcherInterface(
		JoclEventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void init()
	{
		throw Debugging.todo();
	}
}

