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
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public final float readFloat()
		throws EOFException, IOException
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
		throws EOFException, IOException, NullPointerException
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
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void readFully(byte[] __b, int __o, int __l)
		throws EOFException, IOException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
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
			
			// These many characters were read, we might try again
			rv += rc;
		}
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
	
	@Override
	public final long readLong()
		throws EOFException, IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
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
				// {@squirreljme.error ZZ31 The zero byte cannot be represented
				// with a zero value.}
				if (a == 0)
					throw new UTFDataFormatException("ZZ31");
				
				buf[i] = (char)a;
			}
			
			// Double byte
			else if ((a & 0b1110_0000) == 0b1100_0000)
			{
				int b = (queueat < len ? (queue[queueat++] & 0xFF) :
					__in.readUnsignedByte());
				
				// {@squirreljme.error ZZ32 Invalid double byte character.
				// (The byte sequence)}
				if ((b & 0b1100_0000) != 0b1000_0000)
					throw new UTFDataFormatException(String.format(
						"ZZ32 %02x%02x", a, b));
				
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
				
				// {@squirreljme.error ZZ32 Invalid double byte character.
				// (The byte sequence)}
				if (((b & 0b1100_0000) != 0b1000_0000) ||
					((c & 0b1100_0000) != 0b1000_0000))
					throw new UTFDataFormatException(String.format(
						"ZZ32 %02x%02x%02x", a, b, c));
				
				// Decode
				buf[i] = (char)(((a & 0x0F) << 12) | ((b & 0x3F) << 6) |
					(c & 0x3F));
			}
			
			// {@squirreljme.error ZZ30 Invalid byte sequence. (The byte)}
			else
				throw new UTFDataFormatException(String.format("ZZ30 %02x",
					a));
		}
		
		// Convert to string
		return new String(buf);
	}
}

