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
 * Tests parenthesis expressions.
 *
 * @since 2023/06/24
 */
public class TestParenthesisExpression
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/06/24
	 */
	@Override
	public void test()
		throws IOException
	{
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.expression(CExpressionBuilder.builder()
				.parenthesis()
				.parenthesis()
				.parenthesis()
				.close()
				.close()
				.close()
				.build());
			
			this.secondary("empty", spool.tokens());
		}
	}
}
