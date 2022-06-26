// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public abstract class IntBuffer
	extends Buffer
	implements Comparable<IntBuffer>
{
	IntBuffer()
	{
		throw Debugging.todo();
	}
	
	public abstract int get();
	
	public abstract int get(int __a);
	
	@SuppressWarnings({"AbstractMethodOverridesAbstractMethod", "override"})
	public abstract boolean isDirect();
	
	public abstract ByteOrder order();
	
	public abstract IntBuffer put(int __a);
	
	public abstract IntBuffer put(int __a, int __b);
	
	public abstract IntBuffer slice();
	
	@SuppressWarnings({"override"})
	public final int[] array()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
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
	
	public IntBuffer get(int[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public IntBuffer get(int[] __a)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
	public final boolean hasArray()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	public IntBuffer put(IntBuffer __a)
	{
		throw Debugging.todo();
	}
	
	public IntBuffer put(int[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public final IntBuffer put(int[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	public static IntBuffer wrap(int[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public static IntBuffer wrap(int[] __a)
	{
		throw Debugging.todo();
	}
}

