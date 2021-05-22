// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * This encodes binary data to a base64 representation of that data.
 *
 * @since 2021/05/22
 */
public final class Base64Encoder
	extends Reader
{
	/** The stream to read from. */
	protected final InputStream in;
	
	/** The alphabet. */
	private final char[] _alphabet;
	
	/**
	 * Initializes the base64 encoder, using the basic alphabet.
	 * 
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/22
	 */
	public Base64Encoder(InputStream __in)
		throws NullPointerException
	{
		this(__in, Base64Alphabet.BASIC);
	}
	
	/**
	 * Initializes the base64 encoder.
	 * 
	 * @param __in The stream to read from.
	 * @param __alphabet The alphabet to encode/decode from.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/22
	 */
	public Base64Encoder(InputStream __in, Base64Alphabet __alphabet)
		throws NullPointerException
	{
		if (__in == null || __alphabet == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this._alphabet = __alphabet._alphabet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/22
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/05/22
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw Debugging.todo();
	}
}
