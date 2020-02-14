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
 * This class calculates the Adler32 checksum.
 *
 * This class is not thread safe.
 *
 * @since 2017/03/05
 */
public class Adler32Calculator
	implements Checksum
{
	/** The modulo for adler values. */
	private static final int _ADLER_MODULO =
		65521;
	
	/** Single byte read. */
	private final byte[] _solo =
		new byte[1];
	
	/** The A value. */
	private volatile int _a =
		1;
	
	/** The B value. */
	private volatile int _b;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/05
	 */
	@Override
	public final int checksum()
	{
		return (this._b << 16) | this._a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/05
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
	 * @since 2017/03/05
	 */
	@Override
	public final void offer(byte[] __b)
		throws NullPointerException
	{
		offer(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/05
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
		
		// Get parameters
		int a = this._a,
			b = this._b;
		
		// Calculate
		for (int p = __o, e = __o + __l; p < e; p++)
		{
			a = (a + (__b[p] & 0xFF)) % _ADLER_MODULO;
			b = (b + a) % _ADLER_MODULO;
		}
		
		// Set parameters
		this._a = a;
		this._b = b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/05
	 */
	@Override
	public final void reset()
	{
		// Reset both parameters
		this._a = 1;
		this._b = 0;
	}
}

