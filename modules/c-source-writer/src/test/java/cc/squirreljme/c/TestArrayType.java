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
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2),
				"foo"));
			
			this.secondary("int", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.arrayType(3),
				"foo"));
			
			this.secondary("intint", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.constType().arrayType(2),
				"foo"));
			
			this.secondary("constint", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
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
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.pointerType().arrayType(2),
				"foo"));
			
			this.secondary("arrayofpointer", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.pointerType().pointerType(),
				"foo"));
			
			this.secondary("pointerpointerarray", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.arrayType(3).pointerType(),
				"foo"));
			
			this.secondary("pointerarrayarray", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.pointerType().constType(),
				"foo"));
			
			this.secondary("constpointerarray", spool.tokens());
		}
		
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(2)
					.pointerType().constType().pointerType()
					.pointerType().constType(),
				"foo"));
			
			this.secondary("alt", spool.tokens());
		}
		
		// int(*foo[2])[3]
		// declare foo as array 2 of pointer to array 3 of int
		try (__Spool__ spool = __Spool__.__init(false))
		{
			spool.declare(CVariable.of(
				CPrimitiveType.SIGNED_INTEGER.arrayType(3)
					.pointerType().arrayType(2),
				"foo"));
			
			this.secondary("alttwo", spool.tokens());
		}
	}
}
