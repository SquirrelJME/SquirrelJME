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

/**
 * This is a data sink which supports the CRC 32 algorithm.
 *
 * This class is not thread safe.
 *
 * @since 2016/07/16
 */
public class CRC32Calculator
	implements Checksum
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
	
	/** The initial remainder. */
	protected final int initremainder;
	
	/** The CRC Table. */
	final __CRC32Table__ _table;
	
	/** Solo buffer. */
	private final byte[] _solo =
		new byte[1];
	
	/** The work buffer. */
	private final byte[] _work =
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
		this.initremainder = __initrem;
		this._remainder = __initrem;
		
		// Setup table
		this._table = __CRC32Table__.__table(__poly);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/16
	 */
	@Override
	public final int checksum()
	{
		// Return the current CRC
		int rem = this._remainder;
		return (this.reflectremainder ? Integer.reverse(rem) : rem) ^
			this.finalxor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public final void offer(byte __b)
	{
		byte[] solo = this._solo;
		solo[0] = __b;
		offer(solo, 0, 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
	public final void offer(byte[] __b)
		throws NullPointerException
	{
		offer(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/20
	 */
	@Override
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
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/05
	 */
	@Override
	public final void reset()
	{
		// Only the remainder has to be updated
		this._remainder = this.initremainder;
	}
}

