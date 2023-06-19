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
 * Tests structure types.
 *
 * @since 2023/06/12
 */
public class TestStructType
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/06/12
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Build a basic struct
		CStructType struct = new CStructTypeBuilder("foo")
			.member(CPrimitiveType.INT32_T, "xint")
			.member(CPrimitiveType.VOID.pointerType(), "xvoidptr")
			.build();
		
		this.secondary("declare",
			struct.tokens()
				.toArray(new String[0]));
		this.secondary("define",
			struct.tokens()
				.toArray(new String[0]));
	}
}
