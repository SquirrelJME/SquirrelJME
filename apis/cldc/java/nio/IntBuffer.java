// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio;

public abstract class IntBuffer
	extends Buffer
	implements Comparable<IntBuffer>
{
	IntBuffer()
	{
		throw new todo.TODO();
	}
	
	public abstract int get();
	
	public abstract int get(int __a);
	
	public abstract boolean isDirect();
	
	public abstract ByteOrder order();
	
	public abstract IntBuffer put(int __a);
	
	public abstract IntBuffer put(int __a, int __b);
	
	public abstract IntBuffer slice();
	
	public final int[] array()
	{
		throw new todo.TODO();
	}
	
	public final int arrayOffset()
	{
		throw new todo.TODO();
	}
	
	public int compareTo(IntBuffer __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public IntBuffer get(int[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public IntBuffer get(int[] __a)
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
	
	public IntBuffer put(IntBuffer __a)
	{
		throw new todo.TODO();
	}
	
	public IntBuffer put(int[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public final IntBuffer put(int[] __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	public static IntBuffer wrap(int[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public static IntBuffer wrap(int[] __a)
	{
		throw new todo.TODO();
	}
}

