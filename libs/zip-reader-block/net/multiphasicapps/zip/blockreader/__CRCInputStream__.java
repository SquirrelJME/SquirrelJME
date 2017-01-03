// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is used to check that the CRC is valid.
 *
 * @since 2017/01/03
 */
class __CRCInputStream__
	extends InputStream
{
	/** The stream to source from. */
	protected final InputStream in;
	
	/** The final resulting CRC to use. */
	protected final int crc;
	
	/**
	 * Calcualtes the CRC of another given input stream.
	 *
	 * @param __in The stream to read from.
	 * @param __crc The final CRC to calculate.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/01/03
	 */
	__CRCInputStream__(InputStream __in, int __crc)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __in;
		this.crc = __crc;
	}
	
	/**
	 * {@inheritDoc]
	 * @since 2017/01/03
	 */
	@Override
	public void close()
		throws IOException
	{
		// Forward
		this.in.close();
	}
	
	/**
	 * {@inheritDoc]
	 * @since 2017/01/03
	 */
	@Override
	public int read()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc]
	 * @since 2017/01/03
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("AIOB");
		
		throw new Error("TODO");
	}
}

