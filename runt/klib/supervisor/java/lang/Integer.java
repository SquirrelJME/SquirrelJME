// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;

/**
 * This represents a boxed integer.
 *
 * @since 2019/05/25
 */
public final class Integer
	extends Number
{
	/** Maximum value. */
	public static final int MAX_VALUE =
		0x7FFFFFFF;
	
	/** The value of this integer. */
	private transient int _value;
	
	/**
	 * Initializes this integer.
	 *
	 * @param __v The value used.
	 * @since 2019/06/14
	 */
	public Integer(int __v)
	{
		this._value = __v;
	}
	
	/**
	 * Returns a boxed value.
	 *
	 * @param __v The value to use.
	 * @return The boxed value.
	 * @since 2019/05/26
	 */
	public static final Integer valueOf(int __v)
	{
		return new Integer(__v);
	}
}

