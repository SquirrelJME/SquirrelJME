// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Enumeration;

public class Properties
	extends StringKeyHashtable
{
	@Api
	protected Properties defaults;
	
	@Api
	public Properties()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Properties(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Properties(Properties var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized String getProperty(String var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized Integer getIntegerProperty(String var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized String getProperty(String var1, String var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized Integer getIntegerProperty(String var1, Integer var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized String setProperty(String var1, String var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized Integer setProperty(String var1, Integer var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public synchronized Enumeration propertyNames()
	{
		throw Debugging.todo();
	}
}
