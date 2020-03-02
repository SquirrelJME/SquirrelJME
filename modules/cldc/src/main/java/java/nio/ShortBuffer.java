// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio;

public abstract class ShortBuffer
	extends Buffer
	implements Comparable<ShortBuffer>
{
	ShortBuffer()
	{
		throw new todo.TODO();
	}
	
	public abstract short get();
	
	public abstract short get(int __a);
	
	public abstract boolean isDirect();
	
	public abstract ByteOrder order();
	
	public abstract ShortBuffer put(short __a);
	
	public abstract ShortBuffer put(int __a, short __b);
	
	public abstract ShortBuffer slice();
	
	public final short[] array()
	{
		throw new todo.TODO();
	}
	
	public final int arrayOffset()
	{
		throw new todo.TODO();
	}
	
	public int compareTo(ShortBuffer __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public ShortBuffer get(short[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public ShortBuffer get(short[] __a)
	{
		throw new todo.TODO();
	}
	
	public final boolean hasArray()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public ShortBuffer put(ShortBuffer __a)
	{
		throw new todo.TODO();
	}
	
	public ShortBuffer put(short[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public final ShortBuffer put(short[] __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	public static ShortBuffer wrap(short[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public static ShortBuffer wrap(short[] __a)
	{
		throw new todo.TODO();
	}
}


