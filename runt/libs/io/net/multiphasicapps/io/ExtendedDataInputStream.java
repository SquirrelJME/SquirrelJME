// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
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
	
	/** The mark start position. */
	private volatile long _markstart =
		-1L;
	
	/** The mark end position. */
	private volatile long _markend =
		-1L;
	
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
		throw new todo.TODO();
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
		// Nothing to mark or not supported? Do nothing
		if (__rl <= 0 || !this.canmark)
			return;
		
		// Mark the sub-stream
		this.input.mark(__rl);
		
		// Start marker count
		long count = this._count;
		this._markstart = count;
		this._markend = count + __rl;
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public boolean readBoolean()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public byte readByte()
		throws IOException
	{
		// Read value
		int rv = read();
		
		// {@squirreljme.error BD0r End of file reached.}
		if (rv < 0)
			throw new EOFException("BD0r");
		
		// Cast
		return (byte)rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public char readChar()
		throws IOException
	{
		return (char)readShort();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public double readDouble()
		throws IOException
	{
		return Double.longBitsToDouble(readLong());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public float readFloat()
		throws IOException
	{
		return Float.intBitsToFloat(readInt());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void readFully(byte[] __b)
		throws IOException
	{
		readFully(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void readFully(byte[] __b, int __o, int __l)
		throws IOException
	{
		this.input.readFully(__b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int readInt()
		throws IOException
	{
		// Read
		int val = this.input.readInt();
		
		// Increase read count
		this._count += 4; 
		
		// Swap?
		switch (this._endian)
		{
			case BIG: return val;
			case LITTLE: return Integer.reverseBytes(val);
			
				// Unknown
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Reads the next line of bytes, treating them as characters (0-255).
	 *
	 * Reading stops when a newline is encountered, which is discarded from
	 * the input.
	 *
	 * If a carriage return is read then if the next character is a newline,
	 * they are both discarded, otherwise the carriage return is discarded.
	 * Reading stops in either case.
	 *
	 * @return The read line or {@code null} if the end of file was reached
	 * before any bytes were read.
	 * @throws IOException On read errors.
	 * @since 2016/09/18
	 */
	public String readLine()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public long readLong()
		throws IOException
	{
		// Read
		long val = this.input.readLong();
		
		// Increase read count
		this._count += 8; 
		
		// Swap?
		switch (this._endian)
		{
			case BIG: return val;
			case LITTLE: return Long.reverseBytes(val);
			
				// Unknown
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public short readShort()
		throws IOException
	{
		// Read
		short val = this.input.readShort();
		
		// Increase read count
		this._count += 2; 
		
		// Swap?
		switch (this._endian)
		{
			case BIG: return val;
			case LITTLE: return Short.reverseBytes(val);
			
				// Unknown
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int readUnsignedByte()
		throws IOException
	{
		return readByte() & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int readUnsignedShort()
		throws IOException
	{
		return readShort() & 0xFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public String readUTF()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void reset()
		throws IOException
	{
		// {@squirreljme.error BD0s The stream has not been marked.}
		long markstart = this._markstart;
		if (markstart < 0)
			throw new IOException("BD0s");
		
		// {@squirreljme.error BD0t Exceeded the number of bytes specified by
		// mark.}
		long count = this._count;
		long markend = this._markend;
		if (count > markend)
			throw new IOException("BD0t");
		
		// Call reset
		this.input.reset();
		
		// Reset the current count to the start of the mark
		this._count = markstart;
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public int skipBytes(int __n)
		throws IOException
	{
		throw new todo.TODO();
	}
}

