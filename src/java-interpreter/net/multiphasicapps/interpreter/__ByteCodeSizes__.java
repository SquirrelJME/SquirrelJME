// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This class contains the logic which is used to determine the size of
 * operations within a given byte code array.
 *
 * This is needed because all positions where the instructions exist must be
 * known before they can be properly indexed as such.
 *
 * This also acts as an unknown instruction finder also and will fail if one
 * is found.
 *
 * @since 2016/03/29
 */
class __ByteCodeSizes__
{
	/**
	 * Is static only.
	 *
	 * @since 2016/03/29
	 */
	private __ByteCodeSizes__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * This determines the size of the given instruction.
	 *
	 * @param __pos The position of the instruction.
	 * @param __code The instruction code for parsing.
	 * @return The size of the given instruction.
	 * @throws JVMVerifyException If the instruction runs off the code block
	 * or it is unknown or invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	static int __sizeOf(int __pos, byte[] __code)
		throws JVMVerifyException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Could be out of bounds
		int opcode = -1;
		try
		{
			// Read the opcode
			opcode = ((int)__code[__pos]) & 0xFF;
			
			throw new Error("TODO");
		}
		
		// Out of bounds instruction
		catch (IndexOutOfBoundsException e)
		{
			throw new JVMVerifyException(String.format("IN1h %d", opcode), e);
		}
	}
}

