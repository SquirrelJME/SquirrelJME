// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

/**
 * This class provides the ability to read binary data from a stream.
 *
 * All data which is read, is stored as big endian.
 *
 * @since 2018/12/01
 */
public class DataInputStream
	extends InputStream
	implements DataInput
{
	/** The wrapped stream. */
	protected final InputStream in;	
	
	/**
	 * Wraps the specified stream.
	 *
	 * @param __in The stream to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/01
	 */
	public DataInputStream(InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException();
		
		// Set
		in = __in;
	}
	
	@Override
	public int available()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public void close()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public void mark(int __rl)
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean markSupported()
	{
		throw new todo.TODO();
	}
	
	@Override
	public final int read()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final int read(byte[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final int read(byte[] __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final boolean readBoolean()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public final byte readByte()
		throws IOException
	{
		int rv = this.in.read();
		
		if (rv < 0)
			throw new EOFException("EOFF");
		
		return (byte)rv;
	}
	
	@Override
	public final char readChar()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final double readDouble()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final float readFloat()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 3018/12/01
	 */
	@Override
	public final void readFully(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		int rv = 0,
			bl = __b.length;
		
		// Constantly read in as many chunks as possible
		InputStream in = this.in;
		while (rv < bl)
		{
			// Read entire chunk
			int rc = in.read(__b, rv, bl - rv);
			
			// Reached EOF
			if (rc < 0)
				throw new EOFException("EOFF");
			
			// These many characters were read, we might try again
			rv += rc;
		}
	}
	
	@Override
	public final void readFully(byte[] __b, int __o, int __l)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final int readInt()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final long readLong()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final short readShort()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final int readUnsignedByte()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final int readUnsignedShort()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final String readUTF()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public void reset()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public long skip(long __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final int skipBytes(int __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public static final String readUTF(DataInput __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
}

