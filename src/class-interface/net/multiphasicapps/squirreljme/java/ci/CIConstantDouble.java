// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This represents a double constant.
 *
 * @since 2016/04/24
 */
public final class CIConstantDouble
	extends CIConstantValue<Double>
{
	/**
	 * Initializes the constant value.
	 *
	 * @param __v The value to store.
	 * @since 2016/04/24
	 */
	public CIConstantDouble(double __v)
	{
		super(Double.valueOf(__v));
	}
	
	/**
	 * Returns the value of this constant.
	 *
	 * @return The constant value.
	 * @since 2016/04/24
	 */
	public double doubleValue()
	{
		return value.doubleValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPoolTag tag()
	{
		return CIPoolTag.DOUBLE;
	}
}

