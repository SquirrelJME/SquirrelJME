// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.tac.TestBiFunction;

/**
 * Tests a basic bi function.
 *
 * @since 2018/10/06
 */
public class DoBiFunction
	extends TestBiFunction<String, Integer, String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public String test(String __s, Integer __i)
	{
		StringBuilder sb = new StringBuilder(__s + "..." + __i);
		sb.reverse();
		return sb.toString();
	}
}

