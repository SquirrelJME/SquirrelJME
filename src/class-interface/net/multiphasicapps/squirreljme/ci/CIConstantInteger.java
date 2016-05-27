// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci;

/**
 * This represents an integer constant.
 *
 * @since 2016/04/24
 */
public final class CIConstantInteger
	extends CIConstantValue<Integer>
{
	/**
	 * Initializes the constant value.
	 *
	 * @param __v The value to store.
	 * @since 2016/04/24
	 */
	public CIConstantInteger(int __v)
	{
		super(Integer.valueOf(__v));
	}
	
	/**
	 * Returns the value of this constant.
	 *
	 * @return The constant value.
	 * @since 2016/04/24
	 */
	public int intValue()
	{
		return value.intValue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPoolTag tag()
	{
		return CIPoolTag.INTEGER;
	}
}

