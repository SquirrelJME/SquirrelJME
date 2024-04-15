// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This stream is capable of writing binary data to an output stream.
 *
 * @since 2018/11/18
 */
@Api
public class DataOutputStream
	extends OutputStream
	implements DataOutput
{
	/** The underlying stream to write to. */
	@Api
	protected OutputStream out;
	
	/** The number of bytes written. */
	@Api
	protected int written;
	
	/**
	 * Initializes the stream to write to the given destination.
	 *
	 * @param __o The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/18
	 */
	@Api
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
	@Api
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeChar(int __v)
		throws IOException
	{
		this.write((__v >> 8) & 0xFF);
		this.write(__v & 0xFF);
		this.written += 2;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeChars(String __v)
		throws IOException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		for (int i = 0, n = __v.length(); i < n; i++)
		{
			char c = __v.charAt(i);
			
			// Exactly in the manner of writeChar(), so this is a bit
			// inefficient, but it must be this way
			this.write((c >> 8) & 0xFF);
			this.write(c & 0xFF);
			this.written += 2;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeDouble(double __v)
		throws IOException
	{
		long v = Double.doubleToRawLongBits(__v);
		
		OutputStream out = this.out;
		out.write(((int)(v >> 56)) & 0xFF);
		out.write(((int)(v >> 48)) & 0xFF);
		out.write(((int)(v >> 40)) & 0xFF);
		out.write(((int)(v >> 32)) & 0xFF);
		out.write(((int)(v >> 24)) & 0xFF);
		out.write(((int)(v >> 16)) & 0xFF);
		out.write(((int)(v >> 8)) & 0xFF);
		out.write(((int)(v)) & 0xFF);
		this.written += 8;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	public final void writeFloat(float __v)
		throws IOException
	{
		int v = Float.floatToRawIntBits(__v);
		
		OutputStream out = this.out;
		out.write((v >> 24) & 0xFF);
		out.write((v >> 16) & 0xFF);
		out.write((v >> 8) & 0xFF);
		out.write(v & 0xFF);
		this.written += 4;
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
		out.write((__v >> 24) & 0xFF);
		out.write((__v >> 16) & 0xFF);
		out.write((__v >> 8) & 0xFF);
		out.write(__v & 0xFF);
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
		OutputStream out = this.out;
		out.write((int)(__v >> 56) & 0xFF);
		out.write((int)(__v >> 48) & 0xFF);
		out.write((int)(__v >> 40) & 0xFF);
		out.write((int)(__v >> 32) & 0xFF);
		out.write((int)(__v >> 24) & 0xFF);
		out.write((int)(__v >> 16) & 0xFF);
		out.write((int)(__v >> 8) & 0xFF);
		out.write((int)(__v) & 0xFF);
		this.written += 8;
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
		out.write((__v >> 8) & 0xFF);
		out.write(__v & 0xFF);
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
		out.write((n >> 8) & 0xFF);
		out.write(n & 0xFF);
		
		// Load string into character array to more quickly access it
		char[] chars = __v.toCharArray();
		
		// Write string contents in modified UTF-8
		int written = 2;
		for (int i = 0; i < n; i++)
		{
			char c = chars[i];
			
			// Single byte
			if (c >= 0x0001 && c <= 0x007F)
			{
				// As a sort of turbo mode, scan the string to see how many
				// single characters we can write all at once instead of
				// writing call so many times. Since most of the time these
				// single characters will be ones which are read
				int end = i + 1;
				for (; end < n; end++)
				{
					char d = chars[end];
					if (d == 0 || c > 0x007F)
						break;
				}
				
				// Just a single byte
				if (end == i)
				{
					out.write((byte)c);
					
					written += 1;
				}
				
				// Multiple bytes were so, convert and write in bulk
				else
				{
					int xl = end - i;
					
					// Convert to bytes
					byte[] chunk = new byte[xl];
					for (int o = 0; o < xl; o++, i++)
						chunk[o] = (byte)chars[i];
					
					// Write all at once
					out.write(chunk, 0, xl);
					
					written += xl;
				}
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

