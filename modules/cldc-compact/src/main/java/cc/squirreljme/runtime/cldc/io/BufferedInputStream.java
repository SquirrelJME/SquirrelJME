// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * This is an input stream which allows for marking and buffering input.
 *
 * @since 2021/12/04
 */
public class BufferedInputStream
	extends InputStream
{
	/** The stream to wrap. */
	protected final InputStream in;
	
	/** Single byte read. */
	private byte[] _singleByte =
		new byte[1];
	
	/** The internal cache buffer. */
	private byte[] _cache;
	
	/** The current read limit of the last mark. */
	private int _readLimit =
		-1;
	
	/** The current read position in the buffer. */
	private int _readAt =
		-1;
	
	/** The current write position in the buffer. */
	private int _writeAt =
		-1;
	
	/** Was EOF hit when filling the buffer? */
	private boolean _hitEOF;
	
	/**
	 * Initializes the input stream that wraps another stream.
	 * 
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/04
	 */
	public BufferedInputStream(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/04
	 */
	@Override
	public int available()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/04
	 */
	@Override
	public void mark(int __readLimit)
	{
		// {@squirreljme.error ZZ4h Zero or negative read limit for mark.}
		if (__readLimit <= 0)
			throw new IllegalArgumentException("ZZ4h");
		
		// Grow the cache buffer size up to set a maximum read limit
		byte[] cache = this._cache;
		if (cache == null || this._cache.length < __readLimit)
			this._cache = (cache = (cache == null ? new byte[__readLimit] :
				Arrays.copyOf(cache, __readLimit))); 
		
		if (true)
			throw Debugging.todo();
		
		// Set new parameters
		this._readLimit = __readLimit;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/04
	 */
	@Override
	public boolean markSupported()
	{
		// Marking is supported here
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/04
	 */
	@Override
	public int read()
		throws IOException
	{
		// Constantly try to read a single byte
		for (byte[] singleByte = this._singleByte;;)
		{
			int rc = this.__read(singleByte, 0, 1);
			
			// EOF?
			if (rc < 0)
				return rc;
			
			if (rc > 0)
				return singleByte[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/04
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		return this.__read(__b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/12/04
	 */
	@Override
	public void reset()
		throws IOException
	{
		// {@squirreljme.error ZZ4g No mark was previously.}
		if (this._readLimit < 0)
			throw new IOException("ZZ4g");
		
		// The read at position just becomes zero again which is the start
		// of the buffer
		this._readAt = 0;
	}
	
	/**
	 * Reads bytes from the buffer.
	 * 
	 * @param __b The buffer to read into.
	 * @param __o The offset into it.
	 * @param __l The length of the read.
	 * @return The read count or {@code -1} on EOF.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/04
	 */
	private int __read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
	}
}
