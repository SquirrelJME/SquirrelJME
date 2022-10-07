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

public abstract class ShortBuffer
	extends Buffer
	implements Comparable<ShortBuffer>
{
	ShortBuffer()
	{
		throw Debugging.todo();
	}
	
	public abstract short get();
	
	public abstract short get(int __a);
	
	@SuppressWarnings({"AbstractMethodOverridesAbstractMethod", "override"})
	public abstract boolean isDirect();
	
	public abstract ByteOrder order();
	
	public abstract ShortBuffer put(short __a);
	
	public abstract ShortBuffer put(int __a, short __b);
	
	public abstract ShortBuffer slice();
	
	@SuppressWarnings({"override"})
	public final short[] array()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
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
	
	public ShortBuffer get(short[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public ShortBuffer get(short[] __a)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
	public final boolean hasArray()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
	}
	
	public ShortBuffer put(ShortBuffer __a)
	{
		throw Debugging.todo();
	}
	
	public ShortBuffer put(short[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public final ShortBuffer put(short[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	public static ShortBuffer wrap(short[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public static ShortBuffer wrap(short[] __a)
	{
		throw Debugging.todo();
	}
}


