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

public abstract class ByteBuffer
	extends Buffer
	implements Comparable<ByteBuffer>
{
	ByteBuffer()
	{
		throw Debugging.todo();
	}
	
	public abstract FloatBuffer asFloatBuffer();
	
	public abstract IntBuffer asIntBuffer();
	
	public abstract ShortBuffer asShortBuffer();
	
	public abstract byte get();
	
	public abstract byte get(int __a);
	
	public abstract float getFloat();
	
	public abstract float getFloat(int __a);
	
	public abstract int getInt();
	
	public abstract int getInt(int __a);
	
	public abstract short getShort();
	
	public abstract short getShort(int __a);
	
	@SuppressWarnings({"AbstractMethodOverridesAbstractMethod", "override"})
	public abstract boolean isDirect();
	
	public abstract ByteBuffer put(byte __a);
	
	public abstract ByteBuffer put(int __a, byte __b);
	
	public abstract ByteBuffer putFloat(float __a);
	
	public abstract ByteBuffer putFloat(int __a, float __b);
	
	public abstract ByteBuffer putInt(int __a);
	
	public abstract ByteBuffer putInt(int __a, int __b);
	
	public abstract ByteBuffer putShort(short __a);
	
	public abstract ByteBuffer putShort(int __a, short __b);
	
	public abstract ByteBuffer slice();
	
	@SuppressWarnings({"override"})
	public final byte[] array()
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings({"override"})
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
	
	public ByteBuffer get(byte[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public ByteBuffer get(byte[] __a)
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
	
	public final ByteOrder order()
	{
		throw Debugging.todo();
	}
	
	public final ByteBuffer order(ByteOrder __a)
	{
		throw Debugging.todo();
	}
	
	public ByteBuffer put(ByteBuffer __a)
	{
		throw Debugging.todo();
	}
	
	public ByteBuffer put(byte[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public final ByteBuffer put(byte[] __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
	
	public static ByteBuffer allocateDirect(int __a)
	{
		throw Debugging.todo();
	}
	
	public static ByteBuffer wrap(byte[] __a, int __b, int __c)
	{
		throw Debugging.todo();
	}
	
	public static ByteBuffer wrap(byte[] __a)
	{
		throw Debugging.todo();
	}
}

