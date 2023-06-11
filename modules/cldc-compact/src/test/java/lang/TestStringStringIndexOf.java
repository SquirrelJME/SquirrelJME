// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Thats that cases of {@link String#indexOf(String, int)} is valid.
 *
 * @since 2022/10/11
 */
public class TestStringStringIndexOf
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/10/11
	 */
	@Override
	public void test()
	{
		this.secondary("toobig",
			"squirrel".indexOf("squirrels", 0));
		
		this.secondary("rel0",
			"squirrel".indexOf("rel", 0));
		this.secondary("rel2",
			"squirrel".indexOf("rel", 2));
		this.secondary("rel5",
			"squirrel".indexOf("rel", 5));
		this.secondary("rel6",
			"squirrel".indexOf("rel", 6));
		
		this.secondary("uir0",
			"squirrel".indexOf("uir", 0));
		this.secondary("uir2",
			"squirrel".indexOf("uir", 2));
		this.secondary("uir3",
			"squirrel".indexOf("uir", 3));
		
		this.secondary("dojastr",
			"squirreljme+doja://".indexOf("/", 18));
	}
}
