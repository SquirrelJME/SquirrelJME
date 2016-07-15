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
 * {@squirreljme.error BD01 Unhandled endianess. (The endianess)}
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void write(byte[] __b)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		writeByte(__b);
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
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeBytes(String __s)
		throws IOException
	{
		throw new Error("TODO");
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
		DataEndianess endian = this._endian;
		switch (endian)
		{
				// Big
			case BIG:
				writeByte(__v >>> 24);
				writeByte(__v >>> 16);
				writeByte(__v >>> 8);
				writeByte(__v);
				break;
				
				// Little
			case LITTLE:
				writeByte(__v);
				writeByte(__v >>> 8);
				writeByte(__v >>> 16);
				writeByte(__v >>> 24);
				break;
			
				// Unknown
			default:
				throw new IOException(String.format("BD01", endian));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeLong(long __v)
		throws IOException
	{
		// Depends on the endian
		DataEndianess endian = this._endian;
		switch (endian)
		{
				// Big
			case BIG:
				throw new Error("TODO");
				
				// Little
			case LITTLE:
				throw new Error("TODO");
			
				// Unknown
			default:
				throw new IOException(String.format("BD01", endian));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeShort(int __v)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/10
	 */
	@Override
	public final void writeUTF(String __s)
		throws IOException
	{
		throw new Error("TODO");
	}
}

