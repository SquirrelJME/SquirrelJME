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
 * This is an input stream which allows for marking input.
 *
 * @since 2021/12/04
 */
public class MarkableInputStream
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
	public MarkableInputStream(InputStream __in)
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
		// Our available is exactly what our buffer fits
		if (this._readLimit > 0)
			return this._writeAt - this._readAt;
		
		// Otherwise base it on the source stream
		return this.in.available();
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
			
		// Remember the old cache, since we might re-allocate it
		byte[] cache = this._cache;
		byte[] oldCache = cache;
		
		// Grow the cache buffer size up to set a maximum read limit
		if (cache == null || this._cache.length < __readLimit)
			this._cache = (cache = (cache == null ? new byte[__readLimit] :
				Arrays.copyOf(cache, __readLimit)));
		
		// Move everything in the buffer over for the new marking
		int oldReadLimit = this._readLimit;
		int readAt = this._readAt;
		int writeAt = this._writeAt;
		if (oldReadLimit > 0)
		{
			// How many bytes are ready?
			int oldLen = writeAt - readAt;
			
			// Slam all the bytes down
			System.arraycopy((oldCache == null ? cache : oldCache), readAt,
				cache, 0, oldLen);
			
			// Set new position parameters
			this._writeAt = writeAt - oldLen;
		}
		
		// Set new parameters, we will always use the highest read limit
		// we requested so that we need not worry about dropping and
		// chopping buffers or similar
		this._readLimit = Math.max(oldReadLimit, __readLimit);
		this._readAt = 0;
		if (writeAt < 0)
			this._writeAt = 0;
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
		
		InputStream in = this.in;
		
		// If un-marked, just do a direct forwarding read without regarding
		// if we are in a mark or not. Technically this means that the buffered
		// input stream is not actually buffered but we really just want a
		// markable one anyway.
		int readLimit = this._readLimit;
		if (readLimit <= 0)
			return in.read(__b, __o, __l);
			
		// Read parameters
		byte[] cache = this._cache;
		int writeAt = this._writeAt;
		int readAt = this._readAt;
		int cacheReadLimit = writeAt - readAt;
		boolean hitEOF = this._hitEOF;
		
		// If we have a mark, take from the mark buffer first
		int outAt = __o;
		int left = __l;
		if (cacheReadLimit > 0)
		{
			// How much will we be copying in?
			int copyLen = Math.min(cacheReadLimit, left);
			
			// Copy the buffer over
			System.arraycopy(cache, readAt,
				__b, outAt, copyLen);
			
			// Adjust parameters
			outAt += copyLen;
			readAt += copyLen;
			left -= copyLen;
		}
		
		// Read and copy in data accordingly
		while (left > 0)
		{
			throw Debugging.todo();
		}
		
		// Return the number of bytes we read in, make sure to give EOF if
		// we really reached that state
		int total = outAt - __o;
		return (hitEOF && total <= 0 ? -1 : total);
	}
}
