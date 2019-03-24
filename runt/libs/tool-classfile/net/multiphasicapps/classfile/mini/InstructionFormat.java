// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import net.multiphasicapps.classfile.register.RegisterOperationMnemonics;
import net.multiphasicapps.classfile.register.RegisterOperationType;

/**
 * This represents the instruction format that is used for instructions, this
 * simplifies and shares encodings between various instructions.
 *
 * @since 2019/03/24
 */
public enum InstructionFormat
{
	/** End. */
	;
	
	/**
	 * Returns the format that is used for the given operation.
	 *
	 * @param __op The operation to get.
	 * @return The instruction format used.
	 * @throws IllegalArgumentException If the operation is not known.
	 * @since 2019/03/24
	 */
	public static final InstructionFormat of(int __op)
		throws IllegalArgumentException
	{
		switch (__op)
		{
				// {@squirreljme.error JC2m Could not get the format for the
				// given operation. (The operation)}
			default:
				throw new IllegalArgumentException("JC2m " +
					RegisterOperationMnemonics.toString(__op));
		}
	}
}

