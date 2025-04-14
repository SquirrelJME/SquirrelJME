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
		CType type = CFunctionType.of(CIdentifier.of("boop"),
				CPrimitiveType.SIGNED_INTEGER,
				CVariable.of(CPrimitiveType.UNSIGNED_CHAR, "squeak"))
			.pointerType();
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// int (*cute)(unsigned char squeak);
			spool.declare(CVariable.of(type, "cute"));
			
			this.secondary("intboopsqueak", spool.tokens());
		}
		
		// Then pointer for different rule
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// int (*const cute)(unsigned char squeak);
			spool.declare(CVariable.of(type.constType(), "cute"));
			
			this.secondary("const", spool.tokens());
		}
		
		// Pointer pointer
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// int (** cute)(unsigned char squeak);
			spool.declare(CVariable.of(type.pointerType(),
				"cute"));
			
			this.secondary("pointerpointer", spool.tokens());
		}
		
		// Alternating
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// int (*const cute)(unsigned char squeak);
			spool.declare(CVariable.of(type
					.constType().pointerType().pointerType().constType(),
				"cute"));
			
			this.secondary("alt", spool.tokens());
		}
		
		// Array
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// int (*const cute)(unsigned char squeak);
			spool.declare(CVariable.of(type.constType().arrayType(2),
				"cute"));
			
			this.secondary("array", spool.tokens());
		}
		
		// Array of array
		try (__Spool__ spool = __Spool__.__init(false))
		{
			// int (*const cute)(unsigned char squeak);
			spool.declare(CVariable.of(type.constType()
					.arrayType(2).arrayType(3),
				"cute"));
			
			this.secondary("arrayarray", spool.tokens());
		}
		
		// Return an array type
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CFunctionType.of(CIdentifier.of("boop"),
					CPrimitiveType.SIGNED_INTEGER.arrayType(2),
					CVariable.of(CPrimitiveType.UNSIGNED_CHAR,
						"squeak"))
				.pointerType(), "cute"));
			
			this.secondary("returnarray", spool.tokens());
		}
		
		// Return an array array type 
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CFunctionType.of(CIdentifier.of("boop"),
					CPrimitiveType.SIGNED_INTEGER.arrayType(2)
						.arrayType(3),
					CVariable.of(CPrimitiveType.UNSIGNED_CHAR,
						"squeak"))
				.pointerType(), "cute"));
			
			this.secondary("returnarraytwo", spool.tokens());
		}
	}
}
