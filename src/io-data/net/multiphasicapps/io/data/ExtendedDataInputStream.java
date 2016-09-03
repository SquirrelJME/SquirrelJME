// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is an extended input stream which is better suited for general binary
 * data file reading compared to the standard {@link DataInputStream}.
 *
 * Streams default to big endian.
 *
 * @since 2016/07/10
 */
public class ExtendedDataInputStream
	extends InputStream
	implements DataInput, SettableEndianess, SizedStream
{
	/** The original input stream. */
	protected final DataInputStream input;
	
	/** Is mark supported? */
	protected final boolean canmark;
	
	/** The target endianess. */
	private volatile DataEndianess _endian =
		DataEndianess.BIG;
	
	/** The number of bytes read. */
	private volatile long _count;
	
	/**
	 * Initializes the extended input stream.
	 *
	 * @param __is The stream to read data from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/10
	 */
	public ExtendedDataInputStream(InputStream __is)
		throws NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Set
		DataInputStream w;
		this.input = (w = ((__is instanceof DataInputStream) ?
			(DataInputStream)__is : new DataInputStream(__is)));
		
		// Need to know if marking is supported
		this.canmark = w.markSupported();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int available()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void close()
		throws IOException
	{
		this.input.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public DataEndianess getEndianess()
	{
		return this._endian;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void mark(int __rl)
	{
		// Nothing to mark?
		if (__rl <= 0)
			return;
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public boolean markSupported()
	{
		return this.canmark;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int read()
		throws IOException
	{
		int rv = this.input.read();
		
		// Increase count if not EOF
		if (rv >= 0)
			this._count++;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int read(byte[] __b)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public boolean readBoolean()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public byte readByte()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public char readChar()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public double readDouble()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public float readFloat()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void readFully(byte[] __b)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void readFully(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int readInt()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public long readLong()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public short readShort()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int readUnsignedByte()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int readUnsignedShort()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public String readUTF()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void reset()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public DataEndianess setEndianess(DataEndianess __end)
	{
		// Check
		if (__end == null)
			throw new NullPointerException("NARG");
		
		// Get and set
		DataEndianess rv = this._endian;
		this._endian = __end;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public long size()
	{
		return this._count;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public long skip(long __n)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int skipBytes(int __n)
		throws IOException
	{
		throw new Error("TODO");
	}
}

