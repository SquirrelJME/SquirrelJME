// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio;

public abstract class IntBuffer
	extends Buffer
	implements Comparable<IntBuffer>
{
	IntBuffer()
	{
		throw new Error("TODO");
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
		throw new Error("TODO");
	}
	
	public final int arrayOffset()
	{
		throw new Error("TODO");
	}
	
	public int compareTo(IntBuffer __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public IntBuffer get(int[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public IntBuffer get(int[] __a)
	{
		throw new Error("TODO");
	}
	
	public final boolean hasArray()
	{
		throw new Error("TODO");
	}
	
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	public IntBuffer put(IntBuffer __a)
	{
		throw new Error("TODO");
	}
	
	public IntBuffer put(int[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public final IntBuffer put(int[] __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static IntBuffer wrap(int[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static IntBuffer wrap(int[] __a)
	{
		throw new Error("TODO");
	}
}

