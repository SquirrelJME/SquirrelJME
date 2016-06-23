// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio;

public abstract class ShortBuffer
	extends Buffer
	implements Comparable<ShortBuffer>
{
	ShortBuffer()
	{
		throw new Error("TODO");
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
		throw new Error("TODO");
	}
	
	public final int arrayOffset()
	{
		throw new Error("TODO");
	}
	
	public int compareTo(ShortBuffer __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public ShortBuffer get(short[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public ShortBuffer get(short[] __a)
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
	
	public ShortBuffer put(ShortBuffer __a)
	{
		throw new Error("TODO");
	}
	
	public ShortBuffer put(short[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public final ShortBuffer put(short[] __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static ShortBuffer wrap(short[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static ShortBuffer wrap(short[] __a)
	{
		throw new Error("TODO");
	}
}


