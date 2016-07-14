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

public abstract class ByteBuffer
	extends Buffer
	implements Comparable<ByteBuffer>
{
	ByteBuffer()
	{
		super();
		throw new Error("TODO");
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
	
	public final byte[] array()
	{
		throw new Error("TODO");
	}
	
	public final int arrayOffset()
	{
		throw new Error("TODO");
	}
	
	public int compareTo(ByteBuffer __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new Error("TODO");
	}
	
	public ByteBuffer get(byte[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public ByteBuffer get(byte[] __a)
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
	
	public final ByteOrder order()
	{
		throw new Error("TODO");
	}
	
	public final ByteBuffer order(ByteOrder __a)
	{
		throw new Error("TODO");
	}
	
	public ByteBuffer put(ByteBuffer __a)
	{
		throw new Error("TODO");
	}
	
	public ByteBuffer put(byte[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public final ByteBuffer put(byte[] __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	public static ByteBuffer allocateDirect(int __a)
	{
		throw new Error("TODO");
	}
	
	public static ByteBuffer wrap(byte[] __a, int __b, int __c)
	{
		throw new Error("TODO");
	}
	
	public static ByteBuffer wrap(byte[] __a)
	{
		throw new Error("TODO");
	}
}

