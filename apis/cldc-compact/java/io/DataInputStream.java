// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.io;

public class DataInputStream
	extends InputStream
	implements DataInput
{
	/** The wrapped stream. */
	protected final InputStream in;	
	
	public DataInputStream(InputStream __a)
	{
		// Check
		if (__a == null)
			throw new NullPointerException();
		
		// Set
		in = __a;
	}
	
	@Override
	public int available()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public void close()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public void mark(int __rl)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean markSupported()
	{
		throw new Error("TODO");
	}
	
	@Override
	public final int read()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final int read(byte[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final int read(byte[] __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final boolean readBoolean()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final byte readByte()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final char readChar()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final double readDouble()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final float readFloat()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final void readFully(byte[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final void readFully(byte[] __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final int readInt()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final long readLong()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final short readShort()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final int readUnsignedByte()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final int readUnsignedShort()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final String readUTF()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public void reset()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public long skip(long __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public final int skipBytes(int __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static final String readUTF(DataInput __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
}

