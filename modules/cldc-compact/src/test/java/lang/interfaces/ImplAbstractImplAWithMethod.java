// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.interfaces;

/**
 * Extends the abstract method but implements the method.
 *
 * @since 2021/02/19
 */
public class ImplAbstractImplAWithMethod
	extends AbstractImplANoMethod
{
	/**
	 * {@inheritDoc}
	 * @since 2021/02/19
	 */
	@Override
	public int methodA()
	{
		return 13;
	}
}
