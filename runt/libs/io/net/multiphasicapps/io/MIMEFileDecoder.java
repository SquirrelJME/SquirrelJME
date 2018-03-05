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
	protected final int mode;
	
	/** The read filename. */
	protected final String filename; 
	
	/**
	 * Initializes the MIME file decoder from the given set of characters.
	 *
	 * Note that the header of the file is read to determine the mode,
	 * file name, and if it is a valid MIME header.
	 *
	 * @param __in The input source.
	 * @throws IOException If the header could not be read or is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public MIMEFileDecoder(Reader __in)
		throws IOException, NullPointerException
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
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the filename which was read.
	 *
	 * @return The read filename.
	 * @since 2018/03/05
	 */
	public final String filename()
	{
		return this.filename;
	}
	
	/**
	 * Returns the UNIX mode of the stream.
	 *
	 * @return The UNIX mode.
	 * @since 2018/03/05
	 */
	public final int mode()
	{
		return this.mode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/05
	 */
	@Override
	public final int read()
		throws IOException
	{
		throw new todo.TODO();
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

