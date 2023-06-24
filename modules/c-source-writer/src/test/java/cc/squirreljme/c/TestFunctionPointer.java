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
 * Tests function pointers.
 *
 * @since 2023/06/05
 */
public class TestFunctionPointer
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public void test()
		throws IOException
	{
		try (__Spool__ spool = new __Spool__())
		{
			// int (*cute)(unsigned char squeak);
			spool.declare(
				CVariable.of(CFunctionType.of(CIdentifier.of("boop"),
					CPrimitiveType.SIGNED_INTEGER,
					CVariable.of(
						CPrimitiveType.UNSIGNED_CHAR, "squeak"))
				.pointerType(), "cute"));
			
			this.secondary("intboopsqueak", spool.tokens());
		}
	}
}
