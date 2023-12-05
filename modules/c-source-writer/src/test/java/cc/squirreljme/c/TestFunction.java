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
 * Tests functions.
 *
 * @since 2023/06/24
 */
public class TestFunction
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
		// int squeak(const unsigned char boop)
		CFunctionType function = CFunctionType.of(
			CIdentifier.of("squeak"),
			CPrimitiveType.SIGNED_INTEGER,
			CVariable.of(CPrimitiveType.UNSIGNED_CHAR.constType(),
				"boop"));
		
		// Declare function
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(function);
			
			this.secondary("declare", spool.tokens());
		}
		
		// Define function
		try (__Spool__ spool = __Spool__.__init(false))
		{
			try (CFunctionBlock ignored = spool.define(function))
			{
				// Do not care for the body
			}
			
			this.secondary("define", spool.tokens());
		}
	}
}
