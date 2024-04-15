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
public abstract class IntBuffer
	extends Buffer
	implements Comparable<IntBuffer>
{
	IntBuffer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract int get();
	
	@Api
	public abstract int get(int __a);
	
	@Api
	public abstract boolean isDirect();
	
	@Api
	public abstract ByteOrder order();
	
	@Api
	public abstract IntBuffer put(int __a);
	
	@Api
	public abstract IntBuffer put(int __a, int __b);
	
	@Api
	public abstract IntBuffer slice();
	
	@Api
	public final int[] array()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int arrayOffset()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int compareTo(IntBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public IntBuffer get(int[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public IntBuffer get(int[] __a)
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
	public IntBuffer put(IntBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public IntBuffer put(int[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final IntBuffer put(int[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static IntBuffer wrap(int[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static IntBuffer wrap(int[] __a)
	{
		throw Debugging.todo();
	}
}

