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
public abstract class ByteBuffer
	extends Buffer
	implements Comparable<ByteBuffer>
{
	ByteBuffer()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract FloatBuffer asFloatBuffer();
	
	@Api
	public abstract IntBuffer asIntBuffer();
	
	@Api
	public abstract ShortBuffer asShortBuffer();
	
	@Api
	public abstract byte get();
	
	@Api
	public abstract byte get(int __a);
	
	@Api
	public abstract float getFloat();
	
	@Api
	public abstract float getFloat(int __a);
	
	@Api
	public abstract int getInt();
	
	@Api
	public abstract int getInt(int __a);
	
	@Api
	public abstract short getShort();
	
	@Api
	public abstract short getShort(int __a);
	
	@Api
	public abstract boolean isDirect();
	
	@Api
	public abstract ByteBuffer put(byte __a);
	
	@Api
	public abstract ByteBuffer put(int __a, byte __b);
	
	@Api
	public abstract ByteBuffer putFloat(float __a);
	
	@Api
	public abstract ByteBuffer putFloat(int __a, float __b);
	
	@Api
	public abstract ByteBuffer putInt(int __a);
	
	@Api
	public abstract ByteBuffer putInt(int __a, int __b);
	
	@Api
	public abstract ByteBuffer putShort(short __a);
	
	@Api
	public abstract ByteBuffer putShort(int __a, short __b);
	
	@Api
	public abstract ByteBuffer slice();
	
	@Api
	public final byte[] array()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final int arrayOffset()
	{
		throw Debugging.todo();
	}
	
	@Override
	public int compareTo(ByteBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ByteBuffer get(byte[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ByteBuffer get(byte[] __a)
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
	public final ByteOrder order()
	{
		throw Debugging.todo();
	}
	
	@Api
	public final ByteBuffer order(ByteOrder __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ByteBuffer put(ByteBuffer __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public ByteBuffer put(byte[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public final ByteBuffer put(byte[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static ByteBuffer allocateDirect(int __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static ByteBuffer wrap(byte[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static ByteBuffer wrap(byte[] __a)
	{
		throw Debugging.todo();
	}
}

