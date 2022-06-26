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

public abstract class FloatBuffer
	extends Buffer
	implements Comparable<FloatBuffer>
{
	FloatBuffer()
	{
		throw Debugging.todo();
	}
	
	public abstract float get();
	
	public abstract float get(int __a);
	
	@SuppressWarnings({"AbstractMethodOverridesAbstractMethod", "override"})
	public abstract boolean isDirect();
	
	public abstract ByteOrder order();
	
	public abstract FloatBuffer put(float __a);
	
	public abstract FloatBuffer put(int __a, float __b);
	
	public abstract FloatBuffer slice();
	
	@SuppressWarnings({"override"})
	public final float[] array()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
	public final int arrayOffset()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int compareTo(FloatBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	public FloatBuffer get(float[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public FloatBuffer get(float[] __a)
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
	
	public FloatBuffer put(FloatBuffer __a)
	{
		throw Debugging.todo();
	}
	
	public FloatBuffer put(float[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public final FloatBuffer put(float[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	public static FloatBuffer wrap(float[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public static FloatBuffer wrap(float[] __a)
	{
		throw Debugging.todo();
	}
}


