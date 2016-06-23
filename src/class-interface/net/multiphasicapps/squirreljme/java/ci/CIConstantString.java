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
 * This represents a string constant.
 *
 * @since 2016/04/24
 */
public final class CIConstantString
	extends CIConstantValue<String>
	implements CharSequence
{
	/**
	 * Initializes the constant value.
	 *
	 * @param __v The value to store.
	 * @since 2016/04/24
	 */
	public CIConstantString(String __v)
	{
		super(__v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public char charAt(int __i)
	{
		return toString().charAt(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public int length()
	{
		return toString().length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
	{
		return toString().subSequence(__s, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public CIPoolTag tag()
	{
		return CIPoolTag.STRING;
	}
}

