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
	/** The decoder for the data. */
	protected final InputStream decoder;
	
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
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new todo.TODO();
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
				throw new RuntimeException("OOPS");
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
		
		throw new todo.TODO();
	}
}

