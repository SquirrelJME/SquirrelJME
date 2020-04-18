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
import java.util.EventObject;

public class Event
	extends EventObject
{
	public static final String APPLICATION_RELAUNCH_PREFIX =
		"APPLICATION_RELAUNCH: ";
	
	public static final String AUDIO_MUTE = "AUDIO_MUTE";
	
	public static final String SYSTEM_STATE = "SYSTEM_STATE";
	
	public static final String SYSTEM_STATE_NORMAL = "NORMAL";
	
	public static final String SYSTEM_STATE_SHUTDOWN = "SHUTDOWN";
	
	public static final String SYSTEM_STATE_STANDBY = "STANDBY";
	
	public static final String SYSTEM_STATE_STARTUP = "STARTUP";
	
	public static final String VOICE_CALL = "VOICE_CALL";
	
	public Event(String var1, String var2, String var3, Object var4)
	{
		super(null);
		
		throw Debugging.todo();
	}
	
	public Event(String var1, long var2, String var4, Object var5)
	{
		super(null);
		
		throw Debugging.todo();
	}
	
	public Event(String var1, double var2, String var4, Object var5)
	{
		super(null);
		
		throw Debugging.todo();
	}
	
	public Event(String var1, boolean var2, String var3, Object var4)
	{
		super(null);
		
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object var1)
	{
		throw Debugging.todo();
	}
	
	public boolean getBoolean()
	{
		throw Debugging.todo();
	}
	
	public double getDouble()
		throws NumberFormatException
	{
		throw Debugging.todo();
	}
	
	public float getFloat()
		throws NumberFormatException
	{
		throw Debugging.todo();
	}
	
	public Object getInfo()
	{
		throw Debugging.todo();
	}
	
	public int getInt()
		throws NumberFormatException
	{
		throw Debugging.todo();
	}
	
	public long getLong()
		throws NumberFormatException
	{
		throw Debugging.todo();
	}
	
	public String getMessage()
	{
		throw Debugging.todo();
	}
	
	public String getName()
	{
		throw Debugging.todo();
	}
	
	@Override
	public Object getSource()
	{
		throw Debugging.todo();
	}
	
	public String getString()
	{
		throw Debugging.todo();
	}
	
	public long getTimestamp()
	{
		throw Debugging.todo();
	}
	
	public Object getValue()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}
