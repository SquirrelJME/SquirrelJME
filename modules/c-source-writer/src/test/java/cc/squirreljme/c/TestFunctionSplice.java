// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests function splicing.
 *
 * @since 2023/07/15
 */
public class TestFunctionSplice
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public void test()
		throws Throwable
	{
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// Start function
			try (CFunctionBlock outer = spool.define(CFunctionType.of(
				"test", CPrimitiveType.VOID)))
			{
				// Setup splice and write into each sub-block
				try (CFunctionBlockSplices splices = outer.splice(3))
				{
					try (CFunctionBlock block = splices.splice(2))
					{
						block.declare(
							CVariable.of(CPrimitiveType.SIGNED_SHORT,
								"c"));
					}
					
					try (CFunctionBlock block = splices.splice(1))
					{
						block.declare(
							CVariable.of(CPrimitiveType.SIGNED_INTEGER,
								"b"));
					}
					
					try (CFunctionBlock block = splices.splice(0))
					{
						block.declare(
							CVariable.of(CPrimitiveType.SIGNED_LONG,
								"a"));
					}
				}
			}
			
			this.secondary("spliced", spool.tokens());
		}
	}
}
