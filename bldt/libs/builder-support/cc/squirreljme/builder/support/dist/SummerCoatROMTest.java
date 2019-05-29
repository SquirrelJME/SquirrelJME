// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.dist;

import cc.squirreljme.builder.support.TimeSpaceType;

/**
 * This builds the SummerCoat ROM except that it completely uses the test
 * projects and is used to make sure SummerCoat and RatufaCoat work properly
 * with all of the tests.
 *
 * @since 2019/05/29
 */
public class SummerCoatROMTest
	extends SummerCoatROM
{
	/**
	 * Initializes the builder.
	 *
	 * @since 2109/05/29
	 */
	public SummerCoatROMTest()
	{
		super("summercoatrom-test", TimeSpaceType.TEST);
	}
}

