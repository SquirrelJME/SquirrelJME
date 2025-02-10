// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This is a stream is auto detects the first four bytes of the input stream
 * to determine the character encoding that is used, using the method described
 * in RFC 4627 entitled "The application/json Media Type for JavaScript Object
 * Notation (JSON)".
 *
 * The RFC specifies the following detection pattern.
 * 00 00 00 xx  UTF-32BE;
 * 00 xx 00 xx  UTF-16BE;
 * xx 00 00 00  UTF-32LE;
 * xx 00 xx 00  UTF-16LE;
 * xx xx xx xx  UTF-8;
 *
 * @since 2014/08/01
 */
public class UTFDetectISR extends Reader
{
	/** Locking object. */
	private final Object _lock = new Object();
	
	/** Buffer for input stream. */
	private MarkableInputStream _bix;
	
	/** Wrapped input, which is correctly determined. */
	private InputStreamReader _wrk;
	
	/**
	 * Initializes the auto detection stream from the specified stream.
	 *
	 * @param __i Stream to perform detection with.
	 * @since 2014/08/01
	 */
	public UTFDetectISR(InputStream __i)
	{
		// Cannot be null
		if (__i == null)
			throw new NullPointerException("No input stream specified.");
		
		// Init stream
		this._bix = new MarkableInputStream(__i);
	}
	
	/**
	 * Reads characters from the underlying stream.
	 *
	 * @param __b Buffer to read into.
	 * @param __o Offset into buffer.
	 * @param __l Length of read.
	 * @return The number of characters read.
	 * @throws IOException If there was an issue closing the stream.
	 * @since 2014/08/01
	 */
	@Override
	public int read(char[] __b, int __o, int __l)
		throws IOException
	{
		synchronized (this._lock)
		{
			// Need to setup work stream
			if (this._wrk == null)
			{
				// Read in 4 bytes
				this._bix.mark(5);
				int[] f = new int[4];
				for (int i = 0; i < 4; i++)
					f[i] = this._bix.read();
				this._bix.reset();	// Go back
				
				// Based on a pattern
				String css = null;
				if (f[0] == 0 && f[1] == 0 && f[2] == 0 && f[3] != 0)
					css = "UTF-32BE";
				else if (f[0] != 0 && f[1] == 0 && f[2] == 0 && f[3] == 0)
					css = "UTF-32LE";
				else if (f[0] == 0 && f[1] != 0 && f[2] == 0 && f[3] != 0)
					css = "UTF-16BE";
				else if (f[0] != 0 && f[1] == 0 && f[2] != 0 && f[3] == 0)
					css = "UTF-16LE";
				else
					css = "UTF-8";
				
				// Init using specified charset
				this._wrk = new InputStreamReader(this._bix, css);
			}
			
			// Read from work stream
			return this._wrk.read(__b, __o, __l);
		}
	}
	
	/**
	 * Closes this input stream and reliquishes any resources used, also closes
	 * the underlying stream.
	 *
	 * @throws IOException If there was an issue closing the stream.
	 * @since 2014/08/01
	 */
	@Override
	public void close()
		throws IOException
	{
		synchronized (this._lock)
		{
			// Already closed?
			if (this._bix == null)
				return;
			
			// Close all streams known about
			try (Closeable aaa = this._bix; Closeable bbb = this._wrk)
			{
			}
			
			// Whoops
			catch (IOException ioe)
			{
				throw ioe;
			}
			
			// Clear refs
			finally
			{
				this._bix = null;
				this._wrk = null;
			}
		}
	}
}

