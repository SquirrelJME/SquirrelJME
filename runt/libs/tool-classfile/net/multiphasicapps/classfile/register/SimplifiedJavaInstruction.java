// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.Instruction;

/**
 * This represents a simplified Java instruction which modifies the operation
 * that was performed to combine and make it more aliased. The primary goal
 * of this class is to simplify the translator code so it does not have to know
 * about every operation.
 *
 * For example {@code ALOAD_2:[]} will become {@code ALOAD:[2]}.
 *
 * @since 2019/04/03
 */
public final class SimplifiedJavaInstruction
{
	/**
	 * Translates a regular instruction to a simplified instruction.
	 *
	 * @param __inst The instruction to translate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public SimplifiedJavaInstruction(Instruction __inst)
		throws NullPointerException
	{
		if (__inst == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

