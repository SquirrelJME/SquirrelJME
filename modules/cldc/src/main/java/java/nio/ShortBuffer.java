// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public abstract class ShortBuffer
	extends Buffer
	implements Comparable<ShortBuffer>
{
	ShortBuffer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract short get();
	
	@Api
	public abstract short get(int __a);
	
	@Api
	public abstract boolean isDirect();
	
	@Api
	public abstract ByteOrder order();
	
	@Api
	public abstract ShortBuffer put(short __a);
	
	@Api
	public abstract ShortBuffer put(int __a, short __b);
	
	@Api
	public abstract ShortBuffer slice();
	
	@Api
	public final short[] array()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int arrayOffset()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int compareTo(ShortBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ShortBuffer get(short[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ShortBuffer get(short[] __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final boolean hasArray()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public ShortBuffer put(ShortBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ShortBuffer put(short[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final ShortBuffer put(short[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static ShortBuffer wrap(short[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static ShortBuffer wrap(short[] __a)
	{
		throw Debugging.todo();
	}
}


