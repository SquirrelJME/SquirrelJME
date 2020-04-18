// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.event;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

public class EventManager
{
	protected EventManager()
	{
		throw Debugging.todo();
	}
	
	public void addEventListener(String var1, EventListener var2)
		throws NullPointerException, IllegalArgumentException,
		SecurityException
	{
		throw Debugging.todo();
	}
	
	public void addEventListener(String var1, EventListener var2,
	 boolean var3)
		throws NullPointerException, IllegalArgumentException,
		SecurityException
	{
		throw Debugging.todo();
	}
	
	public void addEventListener(String var1, EventListener var2,
		String[] var3)
		throws NullPointerException, IllegalArgumentException,
		SecurityException
	{
		throw Debugging.todo();
	}
	
	public void addEventListener(String var1, EventListener var2, double var3,
		double var5)
		throws NullPointerException, IllegalArgumentException,
		SecurityException
	{
		throw Debugging.todo();
	}
	
	public void addEventListener(String var1, EventListener var2, long var3,
		long var5)
		throws NullPointerException, IllegalArgumentException,
		SecurityException
	{
		throw Debugging.todo();
	}
	
	public Event getCurrent(String var1)
	{
		return null;
	}
	
	public String[] getSystemEventNames()
	{
		throw Debugging.todo();
	}
	
	public void post(Event var1)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	public void registerApplication(String var1, String var2)
		throws ClassNotFoundException, IOException
	{
		throw Debugging.todo();
	}
	
	public void registerApplication(String var1, String var2, boolean var3)
		throws ClassNotFoundException, IOException
	{
		throw Debugging.todo();
	}
	
	public void registerApplication(String var1, String var2, double var3,
		double var5)
		throws ClassNotFoundException, IOException
	{
		throw Debugging.todo();
	}
	
	public void registerApplication(String var1, String var2, long var3,
		long var5)
		throws ClassNotFoundException, IOException
	{
		throw Debugging.todo();
	}
	
	public void registerApplication(String var1, String var2, String[] var3)
		throws ClassNotFoundException, IOException
	{
		throw Debugging.todo();
	}
	
	public void removeEventListener(String var1, EventListener var2)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	public void unregisterApplication(String var1, String var2)
	{
		throw Debugging.todo();
	}
	
	public static EventManager getInstance()
	{
		throw Debugging.todo();
	}
}
