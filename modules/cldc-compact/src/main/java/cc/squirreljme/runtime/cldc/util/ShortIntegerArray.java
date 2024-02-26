// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

/**
 * Wraps a short array to provide integer access to it.
 *
 * @see UnsignedShortIntegerArray
 * @since 2019/05/09
 */
public final class ShortIntegerArray
	extends AbstractIntegerArray
{
	/** The backed array. */
	protected final short[] array;
	
	/**
	 * Initializes the array wrapper.
	 *
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	public ShortIntegerArray(short[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.array = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public final int get(int __i)
	{
		return this.array[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public final void set(int __i, int __v)
	{
		this.array[__i] = (short)__v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public final int size()
	{
		return this.array.length;
	}
}

