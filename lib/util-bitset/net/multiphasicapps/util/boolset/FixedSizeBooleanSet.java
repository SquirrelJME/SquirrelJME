// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.boolset;

/**
 * This is a {@link BooleanSet} which is of a fixed size, it only permits
 * reading and writing from bits that are valid within the set.
 *
 * This class is not thread safe.
 *
 * @since 2016/09/16
 */
public class FixedSizeBooleanSet
	implements BooleanSet
{
	/** The number of bits in the set. */
	protected final int size;
	
	/** Raw boolean data. */
	private final byte[] _bits;
	
	/**
	 * Initializes the fixed size boolean set.
	 *
	 * @param __n The number of bits to store.
	 * @throws IndexOutOfBoundsException If the boolean is out of bounds.
	 * @since 2016/09/16
	 */
	public FixedSizeBooleanSet(int __n)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error CH01 Cannot have a boolean set of a negative
		// size.}
		if (__n < 0)
			throw new IndexOutOfBoundsException("CH01");
		
		// Set and allocate
		this.size = __n;
		this._bits = new byte[__n >>> 3];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public boolean get(int __i)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error CH0e Cannot get a bit which is outside the
		// bounds of the set.}
		if (__i < 0 || __i >= this.size)
			throw new IndexOutOfBoundsException("CH0e");
		
		// Get it
		return (0 != (this._bits[__i >>> 3] & (1 << (__i & 7))));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public boolean set(int __i, boolean __v)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error CH02 Cannot set a bit which is outside the
		// bounds of the set.}
		if (__i < 0 || __i >= this.size)
			throw new IndexOutOfBoundsException("CH02");
		
		// Determine the location and mask of the bit
		int lo = __i >>> 3;
		int mk = 1 << (__i & 7);
		
		// Get the old value
		byte[] bits = this._bits;
		byte bye = bits[lo];
		boolean rv = (0 != (bye & mk));
		
		// If the value is different, flip the bit
		if (rv != __v)
			bits[lo] = (byte)(bye ^ mk);
		
		// Old value
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/16
	 */
	@Override
	public int size()
	{
		return this.size;
	}
}

