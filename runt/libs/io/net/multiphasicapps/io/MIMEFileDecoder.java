// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * This class is used to decode input streams which have been encoded in the
 * MIME Base64 format. This file format is genearted by {@code uuencode -m}.
 * This format usually begins with {@code begin-base64 mode filename} and
 * ends with the padding sequence {@code ====}.
 *
 * @since 2018/03/05
 */
public final class MIMEFileDecoder
	extends InputStream
{
	/** The base reader. */
	protected final Reader in;
	
	/** Read header? */
	private volatile boolean _readheader;
	
	/** Read EOF? */
	private volatile boolean _readeof;
	
	/** The input MIME data. */
	private volatile InputStream _datain;
	
	/** The read mode. */
	private volatile int _mode =
		Integer.MIN_VALUE;
	
	/** The read filename. */
	private volatile String _filename; 
	
	/**
	 * Initializes the MIME file decoder from the given set of characters.
	 *
	 * @param __in The input source.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public MIMEFileDecoder(Reader __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * Returns the filename which was read.
	 *
	 * @return The read filename, {@code null} will be returned if it has not
	 * been read yet or has not been specified.
	 * @since 2018/03/05
	 */
	public final String filename()
	{
		return this._filename;
	}
	
	/**
	 * Returns the UNIX mode of the stream.
	 *
	 * @return The UNIX mode, a negative value will be returned if it has not
	 * been read yet.
	 * @since 2018/03/05
	 */
	public final int mode()
	{
		return this._mode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final int read()
		throws IOException
	{
		byte[] next = new byte[1];
		for (;;)
		{
			int rc = this.read(next, 0, 1);
			if (rc < 0)
				return -1;
			else if (rc == 0)
				continue;
			else if (rc == 1)
				return (next[0] & 0xFF);
			else
				throw new todo.OOPS();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Read EOF, nothing needs to be done
		if (this._readeof)
			return -1;
		
		// Reads the file data
		InputStream datain = this._datain;
		
		// Need to read the MIME header?
		if (!this._readheader)
		{
			Reader in = this.in;
			
			// Read a single line from the input
			StringBuilder sb = new StringBuilder();
			for (;;)
			{
				int ch = in.read();
				
				// {@squirreljme.error BD1i Reached EOF trying to read the
				// MIME file header.}
				if (ch < 0)
					throw new IOException("BD1i");
				
				// End of line
				else if (ch == '\r' || ch == '\n')
					break;
				
				sb.append((char)ch);
			}
			String header = sb.toString();
			
			// {@squirreljme.error BD1j First line of file does not start
			// with "{@code begin-base64 }".}
			if (!header.startsWith("begin-base64 "))
				throw new IOException("BD1j");
			
			// Read mode and filename potentially
			header = header.substring(13).trim();
			int fs = header.indexOf(' ');
			
			// Only if the space is valid
			if (fs >= 1)
			{
				// Read mode
				try
				{
					this._mode = Integer.parseInt(header.substring(0, fs), 8);
				}
			
				// {@squirreljme.error BD1k Could not parse the mode.
				catch (NumberFormatException e)
				{
					throw new IOException("BD1k", e);
				}
				
				// Read filename
				this._filename = header.substring(fs).trim();
			}
			
			// Header read
			this._readheader = true;
			
			// Start reading encoded data
			datain = new Base64Decoder(in, Base64Alphabet.BASIC);
			this._datain = datain;
		}
		
		// Read wrapped data
		int rc = datain.read(__b, __o, __l);
		
		// EOF reached? Attempt read of the footer data
		if (rc < 0)
		{
			Reader in = this.in;
			
			// Read final data
			StringBuilder sb = new StringBuilder();
			for (;;)
			{
				int ch = in.read();
				
				if (ch < 0)
					break;
				
				// Skip whitespace
				else if (ch <= ' ')
					continue;
				
				// Append sequence
				sb.append((char)ch);
			}
			String end = sb.toString().trim();
			
			// {@squirreljme.error BD1l End of the mime file data must end
			// with four equal signs on a single line.
			if (!end.equals("===="))
				throw new IOException("BD1l");
			
			this._readeof = true;
		}
		
		// Return the read count
		return rc;
	}
}

