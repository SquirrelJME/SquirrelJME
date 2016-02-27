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

public abstract class FloatBuffer
	extends Buffer
	implements Comparable<FloatBuffer>
{
	FloatBuffer()
	{
		throw new Error("TODO");
	}
	
	public abstract float get();
	
	public abstract float get(int __a);
	
	public abstract boolean isDirect();
	
	public abstract ByteOrder order();
	
	public abstract FloatBuffer put(float __a);
	
	public abstract FloatBuffer put(int __a, float __b);
	
	public abstract FloatBuffer slice();
	
	public final float[] array()
	{
		throw new Error("TODO");
	}
	
	public final int arrayOffset()
	{
		throw new Error("TODO");
	}
	
	public int compareTo(FloatBuffer __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public FloatBuffer get(float[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public FloatBuffer get(float[] __a)
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
	
	public FloatBuffer put(FloatBuffer __a)
	{
		throw new Error("TODO");
	}
	
	public FloatBuffer put(float[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public final FloatBuffer put(float[] __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static FloatBuffer wrap(float[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static FloatBuffer wrap(float[] __a)
	{
		throw new Error("TODO");
	}
}


