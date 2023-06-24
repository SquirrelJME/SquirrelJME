// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Test of basic types.
 *
 * @since 2023/06/24
 */
public class TestBasicTypes
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public void test()
		throws IOException
	{
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(
				CVariable.of(CPrimitiveType.SIGNED_INTEGER, "foo"));
			
			this.secondary("int", spool.tokens());
		}
	}
}
