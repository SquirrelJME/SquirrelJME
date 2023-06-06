// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	{
		CType a = CFunction.of(CIdentifier.of("boop"),
			CBasicType.JINT,
			CVariable.of(CBasicType.JBOOLEAN, "squeak")).pointerType();
		
		Debugging.debugNote("Tokens: %s%n", a.tokens());
	}
}
