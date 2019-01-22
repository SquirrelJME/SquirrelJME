// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.scrf.RegisterCode;

/**
 * This is a primitive byte code processor which just performs direct
 * translation of stack based operations to a register based format but does
 * not perform any optimizations.
 *
 * @since 2019/01/21
 */
public class PrimitiveByteCodeProcessor
	extends ByteCodeProcessor
{
	/**
	 * Initializes the primitive processor.
	 *
	 * @param __mp The method processor.
	 * @param __bc The input byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/21
	 */
	public PrimitiveByteCodeProcessor(MethodProcessor __mp, ByteCode __bc)
		throws NullPointerException
	{
		super(__mp, __bc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/21
	 */
	@Override
	public RegisterCode process()
		throws ClassProcessException
	{
		throw new todo.TODO();
	}
}

