// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import java.util.Random;

/**
 * This is the random number generator, this allows the seed to be obtained
 * and set so that it is restored properly.
 *
 * {@squirreljme.error BE02 Floating point operations not supported.}
 *
 * @since 2017/02/10
 */
public class GameRandom
	extends Random
{
	/** The current seed. */
	protected volatile long seed;	
	
	/**
	 * Initializes the random number generator with the given seed.
	 *
	 * @param __s The seed to use.
	 * @since 2017/02/10
	 */
	public GameRandom(long __s)
	{
		setSeed(__s);
	}
	
	/**
	 * Returns the current raw seed value.
	 *
	 * @return The current raw seed value.
	 * @since 2017/02/10
	 */
	public long getRawSeed()
	{
		return this.seed;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	protected int next(int __b)
	{
		// Setup new seed value
		long seed = this.seed;
		seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
		this.seed = seed;
		
		// Return from the old seed
		return (int)(seed >>> (48 - __b));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public double nextDouble()
	{
		throw new UnsupportedOperationException("BE02");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public float nextFloat()
	{
		throw new UnsupportedOperationException("BE02");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public double nextGaussian()
	{
		throw new UnsupportedOperationException("BE02");
	}
	
	/**
	 * Sets the current raw seed value.
	 *
	 * @param __s The raw seed value to use.
	 * @since 2017/02/10
	 */
	public void setRawSeed(long __s)
	{
		this.seed = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void setSeed(long __s)
	{
		// Update
		this.seed = (__s ^ 0x5DEECE66DL) & ((1L << 48) - 1);
	}
}

