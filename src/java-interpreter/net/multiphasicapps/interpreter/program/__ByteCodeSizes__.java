// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.program;

import net.multiphasicapps.interpreter.JVMVerifyException;

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
			
			// Wide operations
			if (opcode == 196)
			{
				// Read a new byte
				opcode = ((int)__code[__pos + 1]) & 0xFF;
				
				// Four bytes
				if ((opcode >= 21 && opcode <= 25) ||
					(opcode == 169))
					return 4;
				
				// 6 bytes
				else if (opcode == 132)
					return 6;
			}
			
			// Tableswitch
			else if (opcode == 170)
			{
				// Get padded position, which is rounded up to the next byte
				int ppos = ((__pos + 3) & (~3));
				
				// Read the low and high bytes
				int lo = __ByteUtils__.__readUInt(__code, ppos + 4);
				int hi = __ByteUtils__.__readUInt(__code, ppos + 8);
				
				// Calculate the size
				return (ppos - __pos) + 12 + (4 * ((hi - lo) + 1));
			}
			
			// Lookup switch
			else if (opcode == 171)
			{
				// Get padded position, which is rounded up to the next byte
				int ppos = ((__pos + 3) & (~3));
				
				// Read the pair count
				int np = __ByteUtils__.__readUInt(__code, ppos + 4);
				
				// Calculate the size
				return (ppos - __pos) + 8 + (8 * np);
			}
			
			// These are single byte operations
			else if ((opcode >= 0 && opcode <= 15) ||
				(opcode >= 26 && opcode <= 53) ||
				(opcode >= 59 && opcode <= 131) ||
				(opcode >= 133 && opcode <= 152) ||
				(opcode >= 172 && opcode <= 177) ||
				(opcode >= 190 && opcode <= 191) ||
				(opcode >= 194 && opcode <= 195))
				return 1;
			
			// These are two byte operations
			else if ((opcode == 18) ||
				(opcode == 16) ||
				(opcode >= 21 && opcode <= 25) ||
				(opcode >= 54 && opcode <= 58) ||
				(opcode == 169) ||
				(opcode == 188))
				return 2;
			
			// And three byte operations
			else if ((opcode == 17) ||
				(opcode >= 19 && opcode <= 20) ||
				(opcode == 132) ||
				(opcode >= 153 && opcode <= 168) ||
				(opcode >= 178 && opcode <= 183) ||
				(opcode == 184) ||
				(opcode == 187) ||
				(opcode == 189) ||
				(opcode >= 192 && opcode <= 193) ||
				(opcode >= 198 && opcode <= 199))
				return 3;
			
			// Four bytes
			else if (opcode == 197)
				return 4;
			
			// Five bytes
			else if ((opcode >= 185 && opcode <= 186) ||
				(opcode >= 200 && opcode <= 201))
				return 5;

			// If this point is reached then the instruction is not known.
			throw new JVMVerifyException(String.format(
				"IN1h %d", opcode));
		}
		
		// Out of bounds instruction
		catch (IndexOutOfBoundsException e)
		{
			throw new JVMVerifyException(String.format("IN1h %d", opcode), e);
		}
	}
}

