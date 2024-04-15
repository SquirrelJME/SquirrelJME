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
import java.util.Enumeration;

@Api
public final class SimpleVector
{
	@Api
	public Object[] elementData;
	
	@Api
	public int elementCount;
	
	@Api
	public SimpleVector()
	{
		throw Debugging.todo();
	}
	
	@Api
	public SimpleVector(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void addElement(Object var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean removeElement(Object var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int size()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object elementAt(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public Object lastElement()
	{
		throw Debugging.todo();
	}
	
	@Api
	public boolean contains(Object var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public int indexOf(Object var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void removeElementAt(int var1)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void insertElementAt(Object var1, int var2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void removeAllElements()
	{
		throw Debugging.todo();
	}
	
	@Api
	public Enumeration elements()
	{
		throw Debugging.todo();
	}
	
	static final class SimpleVectorEnumerator
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

