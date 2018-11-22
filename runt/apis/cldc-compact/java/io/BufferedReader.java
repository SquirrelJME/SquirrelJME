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

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;
import cc.squirreljme.runtime.cldc.annotation.ProgrammerTip;
import java.util.Arrays;

/**
 * This is a reader which is backed by a buffer which should increase the
 * efficiency of read operations by allowing for bulk reads for easily.
 *
 * It is recommended to wrap these around {@link InputStreamReader} for
 * example due to that class not being efficient due to character conversions.
 *
 * @since 2018/11/22
 */
@ImplementationNote("This implementation uses a simple flat buffer with a " +
	"length and size.")
public class BufferedReader
	extends Reader
{
	/** Default buffer size. */
	private static final int _DEFAULT_SIZE =
		64;
	
	/** The buffer to source from. */
	private final Reader _in;
	
	/** The buffer, this is a linear buffer, null for close(). */
	private char[] _buf;
	
	/** The buffer limit. */
	private int _limit;
	
	/** The read position of the buffer (which one was read). */
	private int _rp;
	
	/** The write position of the buffer (the valid characters). */
	private int _wp;
	
	/**
	 * Initializes the reader.
	 *
	 * @param __r The reader to source from.
	 * @param __bs The size of the internal buffer.
	 * @throws IllegalArgumentException If the buffer size is zero or
	 * negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/22
	 */
	public BufferedReader(Reader __r, int __bs)
		throws IllegalArgumentException, NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ2x Cannot have a zero or negative buffer
		// size.}
		if (__bs <= 0)
			throw new IllegalArgumentException("ZZ2x");
		
		this._in = __r;
		this._buf = new char[__bs];
		this._limit = __bs;
	}
	
	/**
	 * Initializes the buffer using a default buffer size.
	 *
	 * @param __r The reader to source from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/22
	 */
	public BufferedReader(Reader __r)
		throws NullPointerException
	{
		this(__r, _DEFAULT_SIZE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public void close()
		throws IOException
	{
		// The buffer not existing indicates this is closed
		char[] buf = this._buf;
		if (buf != null)
			this._buf = null;
		
		// Close the underlying stream
		this._in.close();
	}
	
	@Override
	@ProgrammerTip("If the mark length is greater than the length of the " +
	 "internal buffer, it will be re-allocated to fit. Care must be taken " +
	 "depending on how large of a buffer is needed to be stored.")
	public void mark(int __l)
		throws IOException
	{
		// Has been closed?
		char[] buf = this._buf;
		if (buf == null)
			throw new IOException("CLSD");
		
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public boolean markSupported()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public int read()
		throws IOException
	{
		// Has this been closed?
		char[] buf = this._buf;
		if (buf == null)
			throw new IOException("CLSD");
		
		// There are no characters in the buffer? Fill it
		int rp = this._rp;
		if (rp == this._wp)
		{
			// Read up to the limit
			int rc = this._in.read(buf, 0, this._limit);
			
			todo.DEBUG.note("Buffered %d: %s", rc,
				new String(buf));
			
			// EOF reached
			if (rc < 0)
				return -1;
			
			// Set new properties
			rp = 0;
			this._wp = rc;
		}
		
		// Read and return the next character
		int rv = buf[rp++];
		this._rp = rp;
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public int read(char[] __c)
		throws IOException, NullPointerException
	{
		return this.read(__c, 0, __c.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Has been closed?
		char[] buf = this._buf;
		if (buf == null)
			throw new IOException("CLOS");
		
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public String readLine()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public boolean ready()
		throws IOException
	{
		// Has been closed?
		char[] buf = this._buf;
		if (buf == null)
			throw new IOException("CLSD");
		
		// There are characters in the buffer or the stream itself is
		// ready
		return (this._rp < this._wp) || this._in.ready();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/22
	 */
	@Override
	public void reset()
		throws IOException
	{
		// Has been closed?
		char[] buf = this._buf;
		if (buf == null)
			throw new IOException("CLOS");
		
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
}

