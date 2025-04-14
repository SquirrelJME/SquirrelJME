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
		throws IOException
	{
		// The type to be a constant
		CType intType = CPrimitiveType.SIGNED_INTEGER;
		
		// const int
		CType constInt = CModifiedType.of(CConstModifier.CONST, intType);
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(constInt, "foo"));
			
			this.secondary("constint", spool.tokens());
		}
		
		// pointer to const int
		CType ptrConstInt = constInt.pointerType();
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(ptrConstInt, "foo"));
		
			this.secondary("ptrtoconstint", spool.tokens());
		}
		
		// const pointer to int
		CType constPointerInt = CModifiedType.of(CConstModifier.CONST,
			intType.pointerType());
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(constPointerInt, "foo"));
		
			this.secondary("constpointertoint", spool.tokens());
		}
		
		// const pointer to const int
		CType constPointerConstInt = CModifiedType.of(CConstModifier.CONST,
			constInt.pointerType());
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(constPointerConstInt, "foo"));
		
			this.secondary("constpointertoconstint", spool.tokens());
		}
		
		// pointer to const pointer to const int
		// const int * const *var
		CType pTcpTci = constPointerConstInt.pointerType();
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(pTcpTci, "foo"));
		
			this.secondary("pTcpTci", spool.tokens());
		}
		
		// pointer to pointer to const pointer to const int
		// const int * const **var
		CType ppTcpTci = pTcpTci.pointerType();
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(ppTcpTci, "foo"));
		
			this.secondary("ppTcpTci", spool.tokens());
		}
	}
}
