// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonValue;
import java.util.AbstractList;
import java.util.Arrays;

/**
 * This specifies a decoding bit, in essence an action to be performed such
 * as creating an array or closing one.
 *
 * @since 2014/08/04
 */
public final class BaseDecoderBit
	extends AbstractList<Object>
{
	/** The kind. */
	private final BaseDecoderKind _k;
	
	/** Values. */
	private final Object[] _v;
	
	/**
	 * Sets the kind of action to use.
	 *
	 * @param __k Action kind to use.
	 * @param __v Values, uses the input value rather than a copy.
	 * @since 2014/08/04
	 */
	BaseDecoderBit(BaseDecoderKind __k, Object... __v)
	{
		// Cannot be null
		if (__k == null)
			throw new NullPointerException("Null kind specified.");
		
		// Set
		this._k = __k;
		this._v = (__v != null ? Arrays.<Object>copyOf(__v, __v.length) :
			new JsonValue[0]);
	}
	
	/**
	 * Obtains a value from the internal array.
	 *
	 * @param __i Value index to obtain.
	 * @throws IndexOutOfBoundsException If the index exceeds bounds.
	 * @since 2014/08/05
	 */
	@Override
	public Object get(int __i)
	{
		// Return, bounds check is done by Java
		return this._v[__i];
	}
	
	/**
	 * Returns the kind of action to perform.
	 *
	 * @return The action to perform.
	 * @since 2014/08/04
	 */
	public BaseDecoderKind getKind()
	{
		return this._k;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2015/04/12?
	 */
	@Override
	public int size()
	{
		return this._v.length;
	}
}
