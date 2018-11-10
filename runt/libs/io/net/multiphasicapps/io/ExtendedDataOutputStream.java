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

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an extended output stream which is better suited to writing
 * general binaries compared to the standard {@link DataOutputStream}.
 *
 * Streams default to big endian.
 *
 * This class is not thread safe.
 *
 * {@squirreljme.error BD0u Unhandled endianess. (The endianess)}
 *
 * @since 2016/07/10
 */
public class ExtendedDataOutputStream
	extends OutputStream
	implements DataOutput, SettableEndianess, SizedStream
{
	/** The output data stream. */
	protected final DataOutputStream output;
	
	/** The target endianess. */
	private volatile DataEndianess _endian =
		DataEndianess.BIG;
	
	/** The current file size. */
	private volatile long _size;
	
	/**
	 * Initializes the extended data output stream.
	 *
	 * @param __os The stream to write data to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/10
	 */
	public ExtendedDataOutputStream(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = ((__os instanceof DataOutputStream) ?
			(DataOutputStream)__os : new DataOutputStream(__os));
	}
	
	/**
	 * Aligns the output to the given number of bytes.
	 *
	 * @param __n The number of bytes to align to.
	 * @throws IndexOutOfBoundsException If the alignment amount is zero or
	 * negative.
	 * @throws IOException On write errors.
	 * @since 2016/09/11
	 */
	public void align(int __n)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error BD0v Cannot align to zero or a negative
		// amount.}
		if (__n <= 0)
			throw new IndexOutOfBoundsException("BD0v");
		
		// Pad
		while ((size() % __n) != 0)
			write(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void close()
		throws IOException
	{
		this.output.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public void flush()
		throws IOException
	{
		this.output.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final DataEndianess getEndianess()
	{
		return this._endian;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final DataEndianess setEndianess(DataEndianess __end)
		throws NullPointerException
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
	public final long size()
	{
		return this._size;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void write(byte[] __b)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Forward
		this.write(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Write
		this.output.write(__b, __o, __l);
		
		// Add size
		this._size += __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		this.output.write(__b);
		this._size++;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeBoolean(boolean __v)
		throws IOException
	{
		writeByte((__v ? 1 : 0));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeByte(int __v)
		throws IOException
	{
		this.output.write(__v);
		this._size++;
	}
	
	/**
	 * Writes a single byte, if its value is out of range then a write error
	 * occurs.
	 *
	 * @param __v The byte to write.
	 * @throws IOException On out of range or other write errors.
	 * @since 2016/09/14
	 */
	public final void writeByteExact(int __v)
		throws IOException
	{
		// {@squirreljme.error BD0w Byte value out of range.}
		if (__v < -128 || __v > 127)
			throw new IOException("BD0w");
		
		writeByte(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeBytes(String __s)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeChar(int __v)
		throws IOException
	{
		writeShort(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeChars(String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Write all characters
		int n = __s.length();
		for (int i = 0; i < n; i++)
			writeShort(__s.charAt(i));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeDouble(double __v)
		throws IOException
	{
		writeLong(Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeFloat(float __v)
		throws IOException
	{
		writeInt(Float.floatToRawIntBits(__v));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeInt(int __v)
		throws IOException
	{
		// Depends on the endian
		DataOutputStream output = this.output;
		DataEndianess endian = this._endian;
		switch (endian)
		{
				// Big
			case BIG:
				output.writeInt(__v);
				break;
				
				// Little
			case LITTLE:
				output.writeInt(Integer.reverseBytes(__v));
				break;
			
				// Unknown
			default:
				throw new IOException(String.format("BD04", endian));
		}
		
		// Increase
		this._size += 4;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeLong(long __v)
		throws IOException
	{
		// Depends on the endianess
		DataEndianess endian = this._endian;
		DataOutputStream output = this.output;
		switch (endian)
		{
				// Big
			case BIG:
				output.writeLong(__v);
				break;
				
				// Little
			case LITTLE:
				output.writeLong(Long.reverseBytes(__v));
				break;
			
				// Unknown
			default:
				throw new IOException(String.format("BD04", endian));
		}
		
		// Increase
		this._size += 8;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeShort(int __v)
		throws IOException
	{
		// Depends on the endian
		DataEndianess endian = this._endian;
		DataOutputStream output = this.output;
		switch (endian)
		{
				// Big
			case BIG:
				output.writeShort(__v);
				break;
				
				// Little
			case LITTLE:
				output.writeShort(Short.reverseBytes((short)__v));
				break;
			
				// Unknown
			default:
				throw new IOException(String.format("BD04", endian));
		}
		
		// Increase
		this._size += 2;
	}
	
	/**
	 * Writes a single short, if its value is out of range then a write error
	 * occurs.
	 *
	 * @param __v The short to write.
	 * @throws IOException On out of range or other write errors.
	 * @since 2016/09/14
	 */
	public final void writeShortExact(int __v)
		throws IOException
	{
		// {@squirreljme.error BD0x Short value out of range.}
		if (__v < -32768 || __v > 32767)
			throw new IOException("BD0x");
		
		writeShort(__v);
	}
	
	/**
	 * Writes a single unsigned byte, if its value is out of range then a
	 * write error occurs.
	 *
	 * @param __v The unsigned byte to write.
	 * @throws IOException On out of range or other write errors.
	 * @since 2016/09/14
	 */
	public final void writeUnsignedByteExact(int __v)
		throws IOException
	{
		// {@squirreljme.error BD0y Unsigned byte value out of range.}
		if (__v < 0 || __v > 255)
			throw new IOException("BD0y");
		
		writeByte(__v);
	}
	
	/**
	 * Writes a single unsigned byte, if its value is out of range then a
	 * write error occurs.
	 *
	 * @param __v The unsigned short to write.
	 * @throws IOException On out of range or other write errors.
	 * @since 2016/09/14
	 */
	public final void writeUnsignedShortExact(int __v)
		throws IOException
	{
		// {@squirreljme.error BD0z Unsigned short value out of range.}
		if (__v < 0 || __v > 65535)
			throw new IOException("BD0z");
		
		writeShort(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeUTF(String __s)
		throws IOException
	{
		throw new todo.TODO();
	}
}

