// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.DecompressionInputStream;

/**
 * This is an input stream which offers no compression but keeps count of
 * the read bytes.
 *
 * @since 2017/08/22
 */
class __NoDecompressionInputStream__
	extends DecompressionInputStream
{
	/** The stream to wrap. */
	protected final InputStream wrap;
	
	/** The number of read bytes. */
	private volatile long _count;
	
	/**
	 * Initializes the wrapping stream.
	 *
	 * @param __w The stream to read bytes from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/22
	 */
	__NoDecompressionInputStream__(InputStream __w)
		throws NullPointerException
	{
		// Check
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.wrap = __w;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public int available()
		throws IOException
	{
		return this.wrap.available();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public long compressedBytes()
	{
		return this._count;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public boolean detectsEOF()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public int read()
		throws IOException
	{
		int rv = this.wrap.read();
		if (rv >= 0)
			this._count++;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public int read(byte[] __b)
		throws IOException, NullPointerException
	{
		int rc = this.wrap.read(__b);
		if (rc > 0)
			this._count += rc;
		
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		int rc = this.wrap.read(__b, __o, __l);
		if (rc > 0)
			this._count += rc;
		
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/22
	 */
	@Override
	public long uncompressedBytes()
	{
		return this._count;
	}
}

