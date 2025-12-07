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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Map;
import java.util.NoSuchElementException;

@Api
public class Subscriber
{
	@Api
	public static final int SIM_BLOCKED = 4;
	
	@Api
	public static final int SIM_MISSING = 0;
	
	@Api
	public static final int SIM_PIN1 = 2;
	
	@Api
	public static final int SIM_PIN2 = 3;
	
	@Api
	public static final int SIM_READY = 1;
	
	@Api
	public int getAuthenticationState()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public CellularNetwork getNetwork()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getOperator()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getPhoneNumber()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public Map<String, String> getProperties()
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getSlotNumber()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getSubscriberType()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setAuthenticationCode(String __code)
		throws NullPointerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean setAutoregistrationMode(boolean __mode)
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void addListener(SubscriberListener __listener)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Subscriber getByPhoneNumber(String __number)
		throws NullPointerException, SecurityException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Subscriber getBySlotNumber(int __slot)
		throws IllegalArgumentException, NoSuchElementException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Subscriber[] getSubscribers()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void removeListener(SubscriberListener __listener)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
