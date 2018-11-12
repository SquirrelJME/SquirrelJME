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
 * This is a class which reads from an input byte array and returns its data.
 *
 * @since 2018/11/11
 */
public class ByteArrayInputStream
	extends InputStream
{
	/** The buffer to read from. */
	protected byte[] buf;
	
	/** The number of bytes to read. */
	protected int count;
	
	/** The current mark. */
	protected int mark;
	
	/** The position within the byte array. */
	protected int pos;
	
	/**
	 * Wraps the specified byte array.
	 *
	 * @param __b The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public ByteArrayInputStream(byte[] __b)
		throws NullPointerException
	{
		this(__b, 0, __b.length);
	}
	
	/**
	 * Wraps the specified byte array.
	 *
	 * @param __b The array to wrap.
	 * @param __o The offset.
	 * @param __l The length.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public ByteArrayInputStream(byte[] __b, int __o, int __l)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.buf = __b;
		this.pos = __o;
		this.count = Math.min(__o + __l, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public int available()
	{
		synchronized (this)
		{
			return Math.max(0, this.count - Math.max(0, this.pos));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public void close()
		throws IOException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public void mark(int __p)
	{
		synchronized (this)
		{
			this.mark = this.pos;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public boolean markSupported()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public int read()
	{
		synchronized (this)
		{
			int pos = this.pos,
				count = this.count;
			
			// EOF?
			if (pos >= count)
				return -1;
			
			return this.buf[pos];
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) >= __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			// Determine how many bytes we can read
			byte[] buf = this.buf;
			int pos = this.pos,
				count = this.count,
				read = Math.min(__l, count - pos);
			
			// Copy bytes
			for (int i = 0; i < __l; i++)
				__b[__o++] = buf[pos++];
			
			if (read == 0)
				return (pos >= count ? -1 : 0);
			return read;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public void reset()
	{
		synchronized (this)
		{
			this.pos = this.mark;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public long skip(long __n)
	{
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
}

