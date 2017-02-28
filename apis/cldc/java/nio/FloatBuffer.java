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

public abstract class FloatBuffer
	extends Buffer
	implements Comparable<FloatBuffer>
{
	FloatBuffer()
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	public final int arrayOffset()
	{
		throw new todo.TODO();
	}
	
	public int compareTo(FloatBuffer __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public FloatBuffer get(float[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public FloatBuffer get(float[] __a)
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
	
	public FloatBuffer put(FloatBuffer __a)
	{
		throw new todo.TODO();
	}
	
	public FloatBuffer put(float[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public final FloatBuffer put(float[] __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
	
	public static FloatBuffer wrap(float[] __a, int __b, int __c)
	{
		throw new todo.TODO();
	}
	
	public static FloatBuffer wrap(float[] __a)
	{
		throw new todo.TODO();
	}
}


