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
import javax.microedition.midlet.MIDlet;

@Api
public class EventDispatcher
{
	@Api
	public EventDispatcher()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Thread getEventThread()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void start()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setCurrentMIDlet(MIDlet var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setJoclEventDispatcherInterface(EventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static UiEventDispatcherInterface setUiEventDispatcherInterface(
		UiEventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setMIDletEventDispatcherInterface(
		EventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setMediaEventDispatcherInterface(
		EventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setSmafEventDispatcherInterface(EventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setPhraseEventDispatcherInterface(
		EventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void setDialEventDispatcherInterface(EventDispatcherInterface var0)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static synchronized int setOptionalEventDispatcher(
		EventDispatcherInterface var0, int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static int setOptionalEventDispatcher(EventDispatcherInterface var0,
		int var1, String var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static synchronized void dispatchEvent()
	{
		throw Debugging.todo();
	}
}

