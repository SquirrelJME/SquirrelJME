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
public abstract class FloatBuffer
	extends Buffer
	implements Comparable<FloatBuffer>
{
	FloatBuffer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract float get();
	
	@Api
	public abstract float get(int __a);
	
	@Api
	public abstract boolean isDirect();
	
	@Api
	public abstract ByteOrder order();
	
	@Api
	public abstract FloatBuffer put(float __a);
	
	@Api
	public abstract FloatBuffer put(int __a, float __b);
	
	@Api
	public abstract FloatBuffer slice();
	
	@Api
	public final float[] array()
	{
		throw Debugging.todo();
	}
	
	@Api
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
	
	@Api
	public FloatBuffer get(float[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public FloatBuffer get(float[] __a)
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
	public FloatBuffer put(FloatBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public FloatBuffer put(float[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final FloatBuffer put(float[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static FloatBuffer wrap(float[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static FloatBuffer wrap(float[] __a)
	{
		throw Debugging.todo();
	}
}


