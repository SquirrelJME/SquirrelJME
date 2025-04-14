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
 * Test of pointer types.
 *
 * @since 2023/06/24
 */
public class TestPointerType
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
			spool.declare(
				CVariable.of(CPrimitiveType.SIGNED_INTEGER.pointerType(),
					"foo"));
			
			this.secondary("int", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(
				CVariable.of(CPrimitiveType.SIGNED_INTEGER
						.pointerType(CPointerCloseness.FAR),
					"foo"));
			
			this.secondary("intfar", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(
				CVariable.of(CPrimitiveType.SIGNED_INTEGER
						.pointerType(CPointerCloseness.HUGE),
					"foo"));
			
			this.secondary("inthuge", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(
				CVariable.of(CPrimitiveType.SIGNED_INTEGER
						.pointerType(CPointerCloseness.FAR)
						.pointerType(CPointerCloseness.HUGE),
					"foo"));
			
			this.secondary("intfarhuge", spool.tokens());
		}
	}
}
