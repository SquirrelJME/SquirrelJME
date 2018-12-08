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
		128;
	
	/** The buffer to source from. */
	private final Reader _in;
	
	/** The buffer, this is a linear buffer, null for close(). */
	private char[] _buf;
	
	/** The buffer limit. */
	private int _limit;
	
	/** The read position of the buffer (which one to read next). */
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
		
		// Number of characters read
		int rv = 0;
		int rp = this._rp,
			wp = this._wp;
		
		// Drain what remains of the buffer
		int left = wp - rp;
		if (left > 0)
		{
			int lim = (left < __l ? left : __l);
			
			for (; rv < lim; rv++)
				__c[__o++] = buf[rp++];
			
			// Invalidate the buffer
			if (rp == wp)
			{
				this._rp = 0;
				this._wp = 0;
			}
		}
		
		// Nothing was in the buffer, so read directly from the source
		// stream
		Reader in = this._in;
		while (rv < __l)
		{
			int rc = in.read();
			
			// EOF
			if (rc < 0)
				return (rv == 0 ? -1 : rv);
			
			// Add character
			__c[__o++] = (char)rc;
			rv++;
		}
		
		// Return the read count
		return rv;
	}
	
	/**
	 * Reads a line from the input and returns it.
	 *
	 * @return The line which was read, or {@code null} on EOF.
	 * @throws IOException On read errors.
	 * @since 2018/11/22
	 */
	public String readLine()
		throws IOException
	{
		// Has this been closed?
		char[] buf = this._buf;
		if (buf == null)
			throw new IOException("CLSD");
		
		// Read/write positions
		int rp = this._rp,
			wp = this._wp;
		
		// The line is potentially what is left in the buffer perhaps
		// But do not make a super tiny string builder, make a guess as to
		// what the average line length is.
		int diff = wp - rp;
		StringBuilder sb = new StringBuilder((diff > 64 ? diff : 64));
		
		// Continually read data
		Reader in = this._in;
		boolean wasinbuf = false;
		for (;;)
		{
			// Was newline read? Did we stop on a CR?
			boolean readnl = false,
				stoppedoncr = false,
				readeof = false;
			
			// Scan
			int ln = rp;
			if (ln < wp)
			{
				// Was something in the buffer?
				wasinbuf = true;
				
				while (ln < wp)
				{
					// Stop on end of line characters
					char c = buf[ln];
					if ((stoppedoncr = (c == '\r')) || c == '\n')
					{
						readnl = true;
						break;
					}
					
					// Keep going
					ln++;
				}
				
				// Append all the characters in this buffer
				sb.append(buf, rp, ln - rp);
				
				// Discard everything which was appended
				this._rp = (rp = ln);
			}
			
			// Ran out of characters to use?
			if (rp == wp)
			{
				// Read in new characters to the buffer
				int rc = in.read(buf, 0, this._limit);
				
				// EOF was reached, if the buffer was empty then nothing was
				// read anyway
				if (rc < 0)
				{
					// True EOF
					if (!wasinbuf && sb.length() == 0)
						return null;
					
					readeof = true;
				}
				
				// Set new properties
				this._rp = (rp = ln = 0);
				this._wp = (wp = (rc > 0 ? rc : 0));
			}
			
			// Eat newline?
			if (readnl)
			{
				// We stopped on a CR, need to check if the following character
				// is a newline. However since the CRLF pair can end on a
				// buffer read barrier, it must be checked to make sure
				// there is absolutely no connection still.
				if (stoppedoncr)
				{
					// There are characters left in the buffer
					int gap = ln + 1;
					if (gap < wp)
					{
						// Is a newline, so skip it
						if (buf[gap] == '\n')
							rp = ln + 2;
						
						// Otherwise do not
						else
							rp = ln + 1;
					}
					
					// Need to actually read a character, to see what it
					// is
					else
					{
						int rx = in.read();
						
						// If the character is not a newline and is not EOF
						// we will just place it in the buffer as if it
						// were a fresh buffer
						if (rx >= 0 && rx != '\n')
						{
							// Store character state
							buf[wp++] = (char)rx;
							this._rp = rp;
							this._wp = wp;
							
							// Do not do any more of our loop stuff
							break;
						}
						
						// We will be skipping a character in the buffer
						// anyway so ln == wp, so always just skip one
						rp = ln + 1;
					}
				}
				
				// Skip single character
				else
					rp = ln + 1;
				
				// Store the new read position
				this._rp = rp;
				
				// Stop
				break;
			}
			
			// Nothing else to do on EOF
			if (readeof)
				break;
		}
		
		// Use this line
		return sb.toString();
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

