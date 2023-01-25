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

public final class SimpleStack
{
	@Api
	public Object[] elementData;
	
	@Api
	public int size;
	
	public SimpleStack()
	{
		throw Debugging.todo();
	}
	
	public SimpleStack(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object push(Object var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object pop()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object peek()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int size()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean empty()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Enumeration elements()
	{
		throw Debugging.todo();
	}
	
	static final class SimpleStackEnumerator
		implements Enumeration
	{
		@Override
		public boolean hasMoreElements()
		{
			throw Debugging.todo();
		}
		
		@Override
		public Object nextElement()
		{
			throw Debugging.todo();
		}
	}
}
