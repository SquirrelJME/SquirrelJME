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
	/** Format is nothing, just plain operation. */
	PLAIN,
	
	/** 16-bit jump address. */
	J16,
	
	/** Pool reference. */
	POOL16,
	
	/** Pool reference, unsigned 16-bit int. */
	POOL16_U16,
	
	/** Type and 32-bit integer. */
	TI32,
	
	/** 16-bit unsigned integer. */
	U16,
	
	/** uint16 + data type + uint16 + pool ref. */
	U16_DT_U16_POOL16,
	
	/** 16-bit unsigned int, 16-bit jump address. */
	U16_J16,
	
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
			case RegisterOperationType.NOP:
			case RegisterOperationType.RETURN:
				return PLAIN;
			
			case RegisterOperationType.JUMP_ON_EXCEPTION:
				return J16;
			
			case RegisterOperationType.INVOKE_FROM_POOL:
				return POOL16;
			
			case RegisterOperationType.ALLOCATE_CLASS:
				return POOL16_U16;
				
			case RegisterOperationType.NARROW_CONST:
				return TI32;
				
			case RegisterOperationType.COUNT:
			case RegisterOperationType.UNCOUNT:
				return U16;
			
			case RegisterOperationType.WRITE_POINTER_WITH_POOL_OFFSET:
				return U16_DT_U16_POOL16;
			
				// {@squirreljme.error JC2m Could not get the format for the
				// given operation. (The operation)}
			default:
				throw new IllegalArgumentException("JC2m " +
					RegisterOperationMnemonics.toString(__op));
		}
	}
}

