// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.io.CRC32Calculator;
import net.multiphasicapps.zip.ZipCRCConstants;

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
	
	/** CRC calculation. */
	protected final CRC32Calculator crccalc =
		new CRC32Calculator(ZipCRCConstants.CRC_REFLECT_DATA,
			ZipCRCConstants.CRC_REFLECT_REMAINDER,
			ZipCRCConstants.CRC_POLYNOMIAL, ZipCRCConstants.CRC_REMAINDER,
			ZipCRCConstants.CRC_FINALXOR);
	
	/** The number of read bytes. */
	private volatile int _count;
	
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
	public int available()
		throws IOException
	{
		// Forward as it may be calculable
		return this.in.available();
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
		// Read in
		int rv = this.in.read();
		
		// EOF? Check CRC value
		CRC32Calculator crccalc = this.crccalc;
		if (rv < 0)
		{
			// {@squirreljme.error BF0s CRC mismatch. (The expected CRC; The
			// calculated CRC; The number of read bytes})
			int thiscrc = crccalc.checksum(), wantcrc = this.crc;
			if (thiscrc != wantcrc)
				throw new IOException(String.format("BF0s %08x %08x %d",
					wantcrc, thiscrc, this._count));
			
			// EOF
			return -1;
		}
		
		// Calculate
		crccalc.offer((byte)rv);
		this._count += 1;
		
		// Return
		return rv;
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
		
		// Read in
		int rv = this.in.read(__b, __o, __l);
		
		// EOF? Check CRC value
		CRC32Calculator crccalc = this.crccalc;
		if (rv < 0)
		{
			// {@squirreljme.error BF0t CRC mismatch. (The expected CRC; The
			// calculated CRC; The number of read bytes})
			int thiscrc = crccalc.checksum(), wantcrc = this.crc;
			if (thiscrc != wantcrc)
				throw new IOException(String.format("BF0t %08x %08x %d",
					wantcrc, thiscrc, this._count));
			
			// EOF
			return -1;
		}
		
		// Calculate
		crccalc.offer(__b, __o, rv);
		this._count += rv;
		
		// Return
		return rv;
	}
}

