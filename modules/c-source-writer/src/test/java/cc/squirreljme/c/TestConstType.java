// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests const types.
 *
 * @since 2023/06/05
 */
public class TestConstType
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/06/05
	 */
	@Override
	public void test()
	{
		// The type to be a constant
		CType intType = CBasicType.JINT;
		
		// const int
		CType constInt = CModifiedType.of(CConstModifier.CONST, intType);
		this.secondary("constint",
			constInt.tokens().toArray(new String[0]));
		
		// pointer to const int
		CType ptrConstInt = constInt.pointerType();
		this.secondary("ptrtoconstint",
			ptrConstInt.tokens().toArray(new String[0]));
		
		// const pointer to int
		CType constPointerInt = CModifiedType.of(CConstModifier.CONST,
			intType.pointerType());
		this.secondary("constpointertoint",
			constPointerInt.tokens().toArray(new String[0]));
		
		// const pointer to const int
		CType constPointerConstInt = CModifiedType.of(CConstModifier.CONST,
			constInt.pointerType());
		this.secondary("constpointertoconstint",
			constPointerConstInt.tokens().toArray(new String[0]));
		
		// pointer to const pointer to const int
		// const int * const *var
		CType pTcpTci = constPointerConstInt.pointerType();
		this.secondary("pTcpTci",
			pTcpTci.tokens().toArray(new String[0]));
		
		// pointer to pointer to const pointer to const int
		// const int * const **var
		CType ppTcpTci = pTcpTci.pointerType();
		this.secondary("ppTcpTci",
			ppTcpTci.tokens().toArray(new String[0]));
	}
}
