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
 * This stream is capable of writing binary data to an output stream.
 *
 * @since 2018/11/18
 */
public class DataOutputStream
	extends OutputStream
	implements DataOutput
{
	/** The underlying stream to write to. */
	protected OutputStream out;
	
	/** The number of bytes written. */
	protected int written;
	
	/**
	 * Initializes the stream to write to the given destination.
	 *
	 * @param __o The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	public DataOutputStream(OutputStream __o)
		throws NullPointerException
	{
		if (__o == null)
			throw new NullPointerException("NARG");
		
		this.out = __o;
	}
	
	@Override
	public void close()
		throws IOException
	{
		this.out.close();
	}
	
	@Override
	public void flush()
		throws IOException
	{
		this.out.flush();
	}
	
	/**
	 * Returns the current number of bytes which were written.
	 *
	 * @return The number of bytes which were written.
	 * @since 2018/11/18
	 */
	public final int size()
	{
		return this.written;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		synchronized (this)
		{
			this.out.write(__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public void write(byte[] __b)
		throws IOException
	{
		synchronized (this)
		{
			this.out.write(__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		synchronized (this)
		{
			this.out.write(__b, __o, __l);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeBoolean(boolean __v)
		throws IOException
	{
		this.out.write((__v ? 1 : 0));
		this.written += 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeByte(int __v)
		throws IOException
	{
		this.out.write(__v);
		this.written += 1;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeBytes(String __v)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeChar(int __v)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeChars(String __v)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeDouble(double __v)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeFloat(float __v)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeInt(int __v)
		throws IOException
	{
		OutputStream out = this.out;
		out.write(__v >> 24);
		out.write(__v >> 16);
		out.write(__v >> 8);
		out.write(__v);
		this.written += 4;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeLong(long __v)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeShort(int __v)
		throws IOException
	{
		OutputStream out = this.out;
		out.write(__v >> 8);
		out.write(__v);
		this.written += 2;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeUTF(String __v)
		throws IOException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		OutputStream out = this.out;
		
		// Write length of string
		int n = __v.length();
		out.write(n >> 8);
		out.write(n);
		
		// Write string contents in modified UTF-8
		int written = 2;
		for (int i = 0; i < n; i++)
		{
			char c = __v.charAt(i);
			
			// Single byte
			if (c >= 0x0001 && c <= 0x007F)
			{
				out.write((byte)c);
				
				written += 1;
			}
			
			// Double byte
			// as: (char)(((a & 0x1F) << 6) | (b & 0x3F))
			else if (c == 0 || (c >= 0x0080 && c <= 0x07FF))
			{
				out.write(0b110_00000 | ((c >>> 6) & 0x1F));
				out.write(0b10_000000 | (c & 0x3F));
				
				written += 2;
			}
			
			// Triple byte
			// as: (char)(((a & 0x0F) << 12) | ((b & 0x3F) << 6) | (c & 0x3F))
			else
			{
				out.write(0b1110_0000 | ((c >>> 12) & 0x0F));
				out.write(0b10_000000 | ((c >>> 6) & 0x3F));
				out.write(0b10_000000 | (c & 0x3F));
				
				written += 3;
			}
		}
		
		// Record it
		this.written += written;
	}
}

