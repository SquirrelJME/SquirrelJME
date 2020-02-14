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

/**
 * The forwards data from an input stream and calculates the checksum for that
 * data stream.
 *
 * @since 2017/03/05
 */
public class ChecksumInputStream
	extends InputStream
{
	/** The checksum Calculator used. */
	protected final Checksum checksum;
	
	/** Input stream. */
	protected final InputStream in;
	
	/**
	 * Initializes the calculator.
	 *
	 * @param __checksum The checksum calculator to write to.
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	public ChecksumInputStream(Checksum __calc, InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__calc == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.checksum = __calc;
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
		
		// Calculate checksum
		this.checksum.offer((byte)rv);
		
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
		
		// Calculate checksum
		this.checksum.offer(__b, __o, rv);
		
		// Read count
		return rv;
	}
}

