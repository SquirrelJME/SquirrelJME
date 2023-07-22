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
import java.util.Hashtable;

@Api
public class StringKeyHashtable
	extends Hashtable
{
	@Api
	public StringKeyHashtable()
	{
		super(0, 0.0F);
		
		throw Debugging.todo();
	}
	
	@Api
	public StringKeyHashtable(int var1)
	{
		super(0, 0.0F);
		
		throw Debugging.todo();
	}
	
	@Api
	public final Object get(String var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean containsKey(String var1)
	{
		throw Debugging.todo();
	}
	
	@Override
	public final Object put(Object var1, Object var2)
	{
		throw Debugging.todo();
	}
	
	@Override
	protected final void rehash()
	{
		throw Debugging.todo();
	}
}
