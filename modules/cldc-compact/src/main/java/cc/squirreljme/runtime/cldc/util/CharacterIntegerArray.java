// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This wraps a character array and provides integer access to it.
 *
 * @since 2018/10/28
 */
@SquirrelJMEVendorApi
public final class CharacterIntegerArray
	extends AbstractIntegerArray
{
	/** The backed array. */
	@SquirrelJMEVendorApi
	protected final char[] array;
	
	/**
	 * Initializes the array wrapper.
	 *
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/28
	 */
	@SquirrelJMEVendorApi
	public CharacterIntegerArray(char[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.array = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final int get(int __i)
	{
		return this.array[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final void set(int __i, int __v)
	{
		this.array[__i] = (char)__v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public final int size()
	{
		return this.array.length;
	}
}

