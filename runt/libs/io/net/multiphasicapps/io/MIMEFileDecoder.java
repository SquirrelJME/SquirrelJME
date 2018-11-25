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
 * @since 2018/03/05
 */
public final class MIMEFileDecoder
	extends InputStream
{
	/** The input base64 data. */
	protected Base64Decoder mime;
	
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
			throw new todo.TODO();
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
			
			throw new todo.TODO();
		}
	}
}

