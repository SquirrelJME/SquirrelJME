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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

/**
 * This class is used to decode input streams which have been encoded in the
 * MIME Base64 format. This file format is genearted by {@code uuencode -m}.
 * This format usually begins with {@code begin-base64 mode filename} and
 * ends with the padding sequence {@code ====}.
 *
 * This class is not thread safe.
 *
 * @since 2018/03/05
 */
public final class MIMEFileDecoder
	extends InputStream
{
	/** The input base64 data. */
	protected Base64Decoder mime;
	
	/** The read mode. */
	private int _mode =
		Integer.MIN_VALUE;
	
	/** The read filename. */
	private String _filename; 
	
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
		
		// Directly wrap the reader with the MIME decoder which reads from
		// a source reader that is internally maintained
		this.mime = new Base64Decoder(new __SubReader__(__in));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public final int available()
		throws IOException
	{
		return this.mime.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.mime.close();
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
		return this.mime.read();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/25
	 */
	@Override
	public final int read(byte[] __b)
		throws IOException
	{
		return this.mime.read(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		return this.mime.read(__b, __o, __l);
	}
	
	/**
	 * This is a sub-reader which handles parsing of the MIME header and
	 * otherwise just passing the data to the Base64Decoder instance.
	 *
	 * @since 2018/11/25
	 */
	private final class __SubReader__
		extends Reader
	{
		/** The line-by-line reader for data. */
		protected final BufferedReader in;
		
		/** The input character buffer. */
		private final StringBuilder _sb =
			new StringBuilder(80);
		
		/** The current read in the buffer. */
		private int _at;
		
		/** The current limit of the buffer. */
		private int _limit;
		
		/** Did we read the header? */
		private boolean _didheader;
		
		/** Did we read the footer? */
		private boolean _didfooter;
		
		/**
		 * Initializes the sub-reader for the MIME data.
		 *
		 * @param __in The source reader.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/24
		 */
		__SubReader__(Reader __in)
			throws NullPointerException
		{
			if (__in == null)
				throw new NullPointerException("NARG");
			
			this.in = new BufferedReader(__in, 80);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public void close()
			throws IOException
		{
			this.in.close();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public int read()
			throws IOException
		{
			// Read header?
			if (!this._didheader)
				this.__readHeader();
			
			// If the footer was read, this means EOF
			if (this._didfooter)
				return -1;
			
			// Need to read more from the buffer
			int at = this._at,
				limit = this._limit;
			if (at >= limit)
			{
				// Read line next
				if (!this.__readNext())
					return -1;
				
				// Re-read
				at = this._at;
				limit = this._limit;
			}
			
			// Read the next character
			int rv = this._sb.charAt(at);
			this._at = at + 1;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public int read(char[] __c)
			throws IOException
		{
			if (__c == null)
				throw new NullPointerException("NARG");
			
			return this.read(__c, 0, __c.length);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/11/25
		 */
		@Override
		public int read(char[] __c, int __o, int __l)
			throws IOException
		{
			if (__c == null)
				throw new NullPointerException("NARG");
			if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
				throw new IndexOutOfBoundsException("IOOB");
			
			// Read header?
			if (!this._didheader)
				this.__readHeader();
			
			// Where to read from
			StringBuilder sb = this._sb;
			int at = this._at,
				limit = this._limit;
			
			// Read in all characters
			int rv = 0;
			while (rv < __l)
			{
				// Need to read more?
				if (at >= limit)
				{
					// EOF?
					if (!this.__readNext())
						return (rv == 0 ? -1 : rv);
					
					// Re-read
					at = this._at;
					limit = this._limit;
				}
				
				// Read the next character
				__c[__o++] = sb.charAt(at++);
			}
			
			// Store new at position
			this._at = at;
			
			return rv;
		}
		
		/**
		 * Reads the header information.
		 *
		 * @throws IOException On read errors.
		 * @since 2018/11/25
		 */
		private final void __readHeader()
			throws IOException
		{
			BufferedReader in = this.in;
			
			// {@squirreljme.error BD23 Unexpected end of file while trying
			// to read MIME header.}
			String ln = in.readLine();
			if (ln == null)
				throw new IOException("BD23");
			
			// The header is in this format:
			// begin-base64 <unixmode> <filename>
			// {@squirreljme.error BD24 MIME encoded does not start with
			// MIME header.}
			if (!ln.startsWith("begin-base64"))
				throw new IOException("BD24");
			
			// UNIX mode?
			int fs = ln.indexOf(' ');
			if (fs >= 0)
			{
				int ss = ln.indexOf(' ', fs + 1);
				
				// Decode octal mode bits
				MIMEFileDecoder.this._mode = Integer.parseInt(
					ln.substring(fs + 1, (ss < 0 ? ln.length() : ss)), 8);
				
				// Filename?
				if (ss >= 0)
					MIMEFileDecoder.this._filename =
						ln.substring(ss + 1);
			}
			
			// Set as read
			this._didheader = true;
		}
		
		/**
		 * Reads the next line into the character.
		 *
		 * @return If a line was read.
		 * @throws IOException On read errors.
		 * @since 2018/11/25
		 */
		private final boolean __readNext()
			throws IOException
		{
			// {@squirreljme.error BD25 Unexpected EOF while read the MIME
			// file data.}
			String ln = this.in.readLine();
			if (ln == null)
				throw new IOException("BD25");
			
			// End of MIME data?
			if (ln.equals("===="))
			{
				// Footer was read, so EOF now
				this._didfooter = true;
				
				// Was EOF
				return false;
			}
			
			// Fill buffer
			StringBuilder sb = this._sb;
			sb.setLength(0);
			sb.append(ln);
			
			// Set properties
			this._at = 0;
			this._limit = ln.length();
			
			// Was not EOF
			return true;
		}
	}
}

