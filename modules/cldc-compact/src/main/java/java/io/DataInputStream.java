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

/**
 * This class provides the ability to read binary data from a stream.
 *
 * All data which is read, is stored as big endian.
 *
 * @since 2018/12/01
 */
@Api
@SuppressWarnings("DuplicateThrows")
public class DataInputStream
	extends InputStream
	implements DataInput
{
	/** The wrapped stream. */
	@Api
	protected final InputStream in;
	
	/**
	 * Wraps the specified stream.
	 *
	 * @param __in The stream to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/01
	 */
	@Api
	public DataInputStream(InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException();
		
		// Set
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public int available()
		throws IOException
	{
		return this.in.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	public void mark(int __rl)
	{
		this.in.mark(__rl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	public boolean markSupported()
	{
		return this.in.markSupported();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final int read()
		throws IOException
	{
		return this.in.read();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final int read(byte[] __b)
		throws IOException
	{
		return this.in.read(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		return this.in.read(__b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final boolean readBoolean()
		throws EOFException, IOException
	{
		int rv = this.in.read();
		
		if (rv < 0)
			throw new EOFException("EOFF");
		
		return (rv != 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/01
	 */
	@Override
	public final byte readByte()
		throws EOFException, IOException
	{
		int rv = this.in.read();
		
		if (rv < 0)
			throw new EOFException("EOFF");
		
		return (byte)rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final char readChar()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return (char)(((a & 0xFF) << 8) |
			(b & 0xFF));
	}
	
	@Override
	public final double readDouble()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read(),
			c = in.read(),
			d = in.read(),
			e = in.read(),
			f = in.read(),
			g = in.read(),
			h = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b | c | d | e | f | g | h) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return Double.longBitsToDouble((((long)(((a & 0xFF) << 24) |
			((b & 0xFF) << 16) |
			((c & 0xFF) << 8) |
			(d & 0xFF))) << 32L) |
			(((long)((((e & 0xFF) << 24) |
			((f & 0xFF) << 16) |
			((g & 0xFF) << 8) |
			(h & 0xFF)))) & 0xFFFFFFFFL));
	}
	
	@Override
	public final float readFloat()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read(),
			c = in.read(),
			d = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b | c | d) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return Float.intBitsToFloat(((a & 0xFF) << 24) |
			((b & 0xFF) << 16) |
			((c & 0xFF) << 8) |
			(d & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 3018/12/01
	 */
	@Override
	public final void readFully(byte[] __b)
		throws EOFException, IOException, NullPointerException
	{
		this.__readFully(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void readFully(byte[] __b, int __o, int __l)
		throws EOFException, IOException
	{
		this.__readFully(__b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final int readInt()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read(),
			c = in.read(),
			d = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b | c | d) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return ((a & 0xFF) << 24) |
			((b & 0xFF) << 16) |
			((c & 0xFF) << 8) |
			(d & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/14
	 */
	@Override
	public final long readLong()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read(),
			c = in.read(),
			d = in.read(),
			e = in.read(),
			f = in.read(),
			g = in.read(),
			h = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b | c | d | e | f | g | h) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return ((((long)(((a & 0xFF) << 24) |
			((b & 0xFF) << 16) |
			((c & 0xFF) << 8) |
			(d & 0xFF))) << 32L) |
			(((long)(((e & 0xFF) << 24) |
			((f & 0xFF) << 16) |
			((g & 0xFF) << 8) |
			(h & 0xFF))) & 0xFFFFFFFFL));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final short readShort()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return (short)(((a & 0xFF) << 8) |
			(b & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final int readUnsignedByte()
		throws EOFException, IOException
	{
		int rv = this.in.read();
		
		if (rv < 0)
			throw new EOFException("EOFF");
		
		return rv & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final int readUnsignedShort()
		throws EOFException, IOException
	{
		InputStream in = this.in;
		
		// Read all values
		int a = in.read(),
			b = in.read();
		
		// If any were negative then all will be with OR
		if ((a | b) < 0)
			throw new EOFException("EOFF");
		
		// Remap values
		return (int)(((a & 0xFF) << 8) |
			(b & 0xFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final String readUTF()
		throws EOFException, IOException, UTFDataFormatException
	{
		return DataInputStream.readUTF(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	public void reset()
		throws IOException
	{
		this.in.reset();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public long skip(long __n)
		throws IOException
	{
		if (__n < 0)
			return 0;
		
		return this.in.skip(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	public final int skipBytes(int __n)
		throws IOException
	{
		// Try to read as many bytes as possible
		InputStream in = this.in;
		for (int i = 0; i < __n; i++)
			if (in.read() < 0)
				return i;
		
		// Read all the bytes
		return __n;
	}
	
	/**
	 * Reads a full buffer from the stream, throwing {@link EOFException}
	 * if not enough bytes were read.
	 * 
	 * @param __b The buffer to read into.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws IndexOutOfBoundsException The offset and/or length are
	 * negative or exceeds the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/07
	 */
	private void __readFully(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		int rv = 0;
		
		// Constantly read in as many chunks as possible
		InputStream in = this.in;
		while (rv < __l)
		{
			// Read entire chunk
			int rc = in.read(__b, __o + rv, __l - rv);
			
			// Reached EOF
			if (rc < 0)
				throw new EOFException("EOFF");
			
			// These many bytes were read, we might try again
			rv += rc;
		}
	}
	
	/**
	 * Reads a modified-UTF sequence from the input.
	 *
	 * @param __in The input.
	 * @return The decoded string.
	 * @throws EOFException On end of file.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws UTFDataFormatException If the input UTF data is not correct.
	 * @since 2018/12/03
	 */
	@Api
	public static final String readUTF(DataInput __in)
		throws EOFException, IOException, NullPointerException,
			UTFDataFormatException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read length and setup buffer
		int len = __in.readUnsignedShort();
		char[] buf = new char[len];
		
		// Instead of calling read multiple times to get multiple bytes we
		// can just do a full read of the entire length. If the queue only
		// contains single byte characters then we end up in the fast route
		// otherwise once the queue is finished, we will start reading from
		// the stream
		byte[] queue = new byte[len];
		int queueat = 0;
		if (len > 0)
			__in.readFully(queue);
		
		// Read all encoded character data, if EOF ever happens it will be
		// generated for us
		for (int i = 0; i < len; i++)
		{
			// Read character
			int a = (queueat < len ? (queue[queueat++] & 0xFF) :
				__in.readUnsignedByte());
			
			// Single byte
			if ((a & 0b1000_0000) == 0b0000_0000)
			{
				/* {@squirreljme.error ZZ0j The zero byte cannot be represented
				with a zero value.} */
				if (a == 0)
					throw new UTFDataFormatException("ZZ0j");
				
				buf[i] = (char)a;
			}
			
			// Double byte
			else if ((a & 0b1110_0000) == 0b1100_0000)
			{
				int b = (queueat < len ? (queue[queueat++] & 0xFF) :
					__in.readUnsignedByte());
				
				/* {@squirreljme.error ZZ0k Invalid double byte character.
				(The byte sequence)} */
				if ((b & 0b1100_0000) != 0b1000_0000)
					throw new UTFDataFormatException(String.format(
						"ZZ0k %02x%02x", a, b));
				
				// Decode
				buf[i] = (char)(((a & 0x1F) << 6) | (b & 0x3F));
			}
			
			// Triple byte
			else if ((a & 0b1111_0000) == 0b1110_0000)
			{
				// Can we quickly read at least one byte from the stream?
				int b, c;
				if (queueat < len)
				{
					b = queue[queueat++] & 0xFF;
					c = (queueat < len ? (queue[queueat++] & 0xFF) :
						__in.readUnsignedByte());
				}
				
				// Nothing
				else
				{
					b = __in.readUnsignedByte();
					c = __in.readUnsignedByte();
				}
				
				/* {@squirreljme.error ZZ0l Invalid double byte character.
				(The byte sequence)} */
				if (((b & 0b1100_0000) != 0b1000_0000) ||
					((c & 0b1100_0000) != 0b1000_0000))
					throw new UTFDataFormatException(String.format(
						"ZZ0l %02x%02x%02x", a, b, c));
				
				// Decode
				buf[i] = (char)(((a & 0x0F) << 12) | ((b & 0x3F) << 6) |
					(c & 0x3F));
			}
			
			/* {@squirreljme.error ZZ0m Invalid byte sequence. (The byte)} */
			else
				throw new UTFDataFormatException(String.format("ZZ0m %02x",
					a));
		}
		
		// Convert to string
		return new String(buf);
	}
}

