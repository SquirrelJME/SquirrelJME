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
 * Class replaces method in super class.
 *
 * @since 2021/02/14
 */
public class SubClassImplAIsReplaced
	extends ImplementsA
{
	/**
	 * {@inheritDoc}
	 * @since 2021/02/14
	 */
	@Override
	public int methodA()
	{
		return 42;
	}
}
