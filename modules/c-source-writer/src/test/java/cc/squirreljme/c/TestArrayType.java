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
 * Tests array types.
 *
 * @since 2023/06/24
 */
public class TestArrayType
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
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2),
				"foo"));
			
			this.secondary("int", spool.tokens());
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.arrayType(3),
				"foo"));
			
			this.secondary("intint", spool.tokens());
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.constType().arrayType(2),
				"foo"));
			
			this.secondary("constint", spool.tokens());
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			try
			{
				spool.declare(CVariable.of(
					CPrimitiveType.SIGNED_INTEGER.arrayType(2)
						.constType(),
					"foo"));
			}
			catch (Throwable t)
			{
				this.secondary("constarray", true);
			}
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2).pointerType(),
				"foo"));
			
			this.secondary("pointerarray", spool.tokens());
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.pointerType().pointerType(),
				"foo"));
			
			this.secondary("pointerpointerarray", spool.tokens());
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.arrayType(3).pointerType(),
				"foo"));
			
			this.secondary("pointerarrayarray", spool.tokens());
		}
		
		try (__Spool__ spool = new __Spool__())
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.pointerType().constType(),
				"foo"));
			
			this.secondary("constpointerarray", spool.tokens());
		}
	}
}
