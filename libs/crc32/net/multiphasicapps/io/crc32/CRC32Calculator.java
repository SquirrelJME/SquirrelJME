// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.crc32;

/**
 * This is a data sink which supports the CRC 32 algorithm.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/16
 */
public class CRC32Calculator
{
	/** Working buffer size. */
	private static final int _WORK_BUFFER =
		32;
	
	/** The polynomial to use. */
	protected final int polynomial;
	
	/** The final XOR value. */
	protected final int finalxor;
	
	/** Reflect the data? */
	protected final boolean reflectdata;
	
	/** Reflect the remainder? */
	protected final boolean reflectremainder;
	
	/** The CRC Table. */
	final __CRC32Table__ _table;
	
	/** The work buffer. */
	private byte[] _work =
		new byte[_WORK_BUFFER];
	
	/** The current CRC value (remainder). */
	private volatile int _remainder;
	
	/**
	 * Initializes the CRC-32 data sink.
	 *
	 * @param __rdata Reflect the data?
	 * @param __rrem Reflect the remainder?
	 * @param __poly The polynomial.
	 * @param __initrem The initial remainder.
	 * @param __fxor The value to XOR the remainder with on return.
	 * @since 2016/07/16
	 */
	public CRC32Calculator(boolean __rdata, boolean __rrem, int __poly,
		int __initrem, int __fxor)
	{
		// Set
		this.reflectdata = __rdata;
		this.reflectremainder = __rrem;
		this.polynomial = __poly;
		this.finalxor = __fxor;
		this._remainder = __initrem;
		
		// Setup table
		this._table = __CRC32Table__.__table(__poly);
	}
	
	/**
	 * Returns the currently calculated CRC value.
	 *
	 * @return The current CRC value.
	 * @since 2016/07/16
	 */
	public int crc()
	{
		// Return the current CRC
		int rem = this._remainder;
		return (this.reflectremainder ? Integer.reverse(rem) : rem) ^
			this.finalxor;
	}
	
	/**
	 * Offers a single byte for CRC calcualtion.
	 *
	 * @param __b The byte to offer.
	 * @since 2016/
	 */
	public final void offer(byte __b)
	{
		offer(new byte[]{__b}, 0, 1);
	}
	
	/**
	 * Offers multiple byte for CRC calculation.
	 *
	 * @param __b The bytes to offer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/20
	 */
	public final void offer(byte[] __b)
		throws NullPointerException
	{
		offer(__b, 0, __b.length);
	}
	
	/**
	 * Offers multiple byte for CRC calculation.
	 *
	 * @param __b The bytes to offer.
	 * @param __o The starting offset to read bytes from.
	 * @param __l The number of bytes to buffer.
	 * @throws ArrayIndexOutOfBoundsException If the offset or length are
	 * negative or they exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/20
	 */
	public final void offer(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("BAOB");
		
		// Read data into the work buffer
		boolean reflectdata = this.reflectdata;
		int remainder = this._remainder;
		int[] table = this._table._table;
		for (int i = __o, end = __o + __l; i < end; i++)
		{
			// Read in data value
			int val = __b[i] & 0xFF;
		
			// Reflect the data?
			if (reflectdata)
				val = Integer.reverse(val) >>> 24;
		
			int d = (val ^ (remainder >>> 24));
			remainder = table[d] ^ (remainder << 8);
		}
		
		// Set new remainder
		this._remainder = remainder;
	}
}

