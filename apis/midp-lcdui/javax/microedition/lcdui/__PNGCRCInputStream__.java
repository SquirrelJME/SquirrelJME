// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.crc32.CRC32Calculator;

/**
 * This processes the input stream and calculates the CRC.
 *
 * @since 2017/02/28
 */
class __PNGCRCInputStream__
	extends InputStream
{
	/** The CRC Calculator used. */
	protected final CRC32Calculator crc;
	
	/** Input stream. */
	protected final InputStream in;
	
	/**
	 * Initializes the calculator.
	 *
	 * @param __crc The CRC calculator to write to.
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	__PNGCRCInputStream__(CRC32Calculator __calc, InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__calc == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.crc = __calc;
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/28
	 */
	@Override
	public int available()
		throws IOException
	{
		return this.in.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/28
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/28
	 */
	@Override
	public int read()
		throws IOException
	{
		int rv = this.in.read();
		
		// EOF?
		if (rv < 0)
			return rv;
		
		// Calculate CRC
		this.crc.offer((byte)rv);
		
		// Return
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/28
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		// Read data
		int rv = this.in.read(__b, __o, __l);
		
		// EOF?
		if (rv < 0)
			return rv;
		
		// Calculate CRC
		this.crc.offer(__b, __o, rv);
		
		// Read count
		return rv;
	}
}

