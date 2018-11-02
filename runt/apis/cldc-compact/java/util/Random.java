// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This is an insecure pseudo-random number generator.
 *
 * This class is thread safe however if multiple threads are using a single
 * instance then it is undefined if their order is the same.
 *
 * @since 2018/11/02
 */
@ImplementationNote("This code essentially is a copy and paste from the " +
	"library documentation since it must always be the same exact code. " +
	"Since there is only one way to implement this class it should be " +
	"legal to do so for compatibility purposes.")
public class Random
{
	/** The current seed value. */
	private long _seed;
	
	/** The next gaussian value. */
	private double _nextg;
	
	/** Has next next gaussian value? */
	private boolean _hasnng;
	
	/**
	 * Initializes the random number generator using an unspecified and
	 * potentially different key per construction.
	 *
	 * @since 2018/11/02
	 */
	public Random()
	{
		this(System.currentTimeMillis() +
			System.nanoTime());
	}
	
	/**
	 * Initializes the random number generator with the given seed.
	 *
	 * @param __seed The seed to initialize with.
	 * @since 2018/11/02
	 */
	public Random(long __seed)
	{
		this.setSeed(__seed);
	}
	
	/**
	 * Generates the next pseudorandom number, this method is used by every
	 * other method in this class. As such this is the only method that has
	 * to be changed if the internal algorithm is to be changed.
	 *
	 * @param __bits The number of bits to return.
	 * @since 2018/11/02
	 */
	protected int next(int __bits)
	{
		// This has to be atomic
		synchronized (this)
		{
			// Update the seed
			long seed = (this._seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
			this._seed = seed;
			
			return (int)(seed >>> (48 - __bits));
		}
	}
	
	/**
	 * Returns the next boolean value.
	 *
	 * @return The next value.
	 * @since 2018/11/02
	 */
	public boolean nextBoolean()
	{
		return this.next(1) != 0;
	}
	
	/**
	 * Generates random bytes and places them into the array.
	 *
	 * @param __a The output array.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/02
	 */
	public void nextBytes(byte[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		for (int i = 0, l = __a.length; i < l;)
			for (int r = this.nextInt(), n = Math.min(l - i, 4); n-- > 0;
				r >>= 8)
				__a[i++] = (byte)r;
	}
	
	/**
	 * Returns the next double value.
	 *
	 * @return The next value.
	 * @since 2018/11/02
	 */
	public double nextDouble()
	{
		return (((long)this.next(26) << 27) + this.next(27)) /
			(double)(1L << 53);
	}
	
	/**
	 * Returns the next float value.
	 *
	 * @return The next value.
	 * @since 2018/11/02
	 */
	public float nextFloat()
	{
		return this.next(24) / ((float)(1 << 24));
	}
	
	/**
	 * Return the next gaussian value.
	 *
	 * @return The next value.
	 * @since 2018/11/02
	 */
	public double nextGaussian()
	{
		synchronized (this)
		{
			// If there was already a cached value, use it
			boolean hasnng = this._hasnng;
			if (hasnng)
			{
				this._hasnng = false;
				return this._nextg;
			}
			
			// Otherwise generate it
			else
			{
				double v1, v2, s;
				do
				{
					// Between -1.0 and 1.0
					v1 = 2 * nextDouble() - 1;
					v2 = 2 * nextDouble() - 1;

					s = v1 * v1 + v2 * v2;
				} while (s >= 1 || s == 0);
				
				double multiplier =
					Random.__strictSqrt(-2 * Random.__strictLog(s) / s);
				
				// Store for next time
				this._nextg = v2 * multiplier;
				this._hasnng = true;
				
				// Return generated value
				return v1 * multiplier;
			}
		}
	}
	
	/**
	 * Returns the next int value.
	 *
	 * @return The next value.
	 * @since 2018/11/02
	 */
	public int nextInt()
	{
		return this.next(32);
	}
	
	/**
	 * Returns the next int value between inclusive 0 and exclusive
	 * {@code __cap}.
	 *
	 * @param __cap The exclusive value to end at.
	 * @return The next value.
	 * @throws IllegalArgumentException If the cap is zero or negative.
	 * @since 2018/11/02
	 */
	public int nextInt(int __cap)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ2x Cap value must be a positive integer}
		if (__cap <= 0)
			throw new IllegalArgumentException("ZZ2x");
		
		// Cap is a power of two
		if ((__cap & -__cap) == __cap)
			return (int)((__cap * (long)this.next(31)) >> 31);
		
		// Otherwise calculate based on values in the range
		int bits, val;
		do
		{
			bits = this.next(31);
			val = bits % __cap;
		} while (bits - val + (__cap - 1) < 0);
		
		return val;
	}
	
	/**
	 * Returns the next long value.
	 *
	 * @return The next long value.
	 * @since 2018/11/02
	 */
	public long nextLong()
	{
		return ((long)this.next(32) << 32) + this.next(32);
	}
	
	/**
	 * Sets the seed for this random number generator.
	 *
	 * This is an atomic operation.
	 *
	 * @param __seed The seed to use.
	 * @since 2018/11/02
	 */
	public void setSeed(long __seed)
	{
		synchronized (this)
		{
			this._seed = (__seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
			this._hasnng = false;
		}
	}
	
	/**
	 * Strict logarithm implementation.
	 *
	 * @param __v The input value.
	 * @return The resulting logarithm value.
	 * @since 2018/11/02
	 */
	private static double __strictLog(double __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Strict square root implementation, needed by {@link #nextGaussian()}.
	 *
	 * @param __v The input value.
	 * @return The resulting square root value.
	 * @since 2018/11/02
	 */
	private static double __strictSqrt(double __v)
	{
		throw new todo.TODO();
	}
}


