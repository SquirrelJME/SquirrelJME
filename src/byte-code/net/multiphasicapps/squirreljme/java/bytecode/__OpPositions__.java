// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.bytecode;

import net.multiphasicapps.squirreljme.java.ci.CIByteBuffer;

/**
 * This calculates the position of all operations.
 *
 * @since 2016/05/08
 */
final class __OpPositions__
{
	/** The code buffer. */
	protected final CIByteBuffer buffer;
	
	/**
	 * Initializes the position calculator.
	 *
	 * @param __bb The buffer to determine positions for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/08
	 */
	__OpPositions__(CIByteBuffer __bb)
		throws NullPointerException
	{
		// Check
		if (__bb == null)
			throw new NullPointerException("NARG");
		
		// Set
		buffer = __bb;
	}
	
	/**
	 * Returns the positions of all operations.
	 *
	 * @return The operation positions.
	 * @since 2016/05/08
	 */
	public int[] get()
	{
		// Setup initial buffer which matches the number of bytes
		CIByteBuffer buffer = this.buffer;
		int len = buffer.length();
		int[] build = new int[len];
		
		// Go through all operation
		int i, a;
		for (i = 0, a = 0; i < len;)
		{
			// Get the operation size
			int sz = __sizeOf(i);
			
			// {@squirreljme.error AX04 The size of the current operation is
			// zero or negative. (The opcode position; The size of it)}
			if (sz <= 0)
				throw new BCException(String.format("AX04 %d %d", i, sz));
			
			// Current operation is here
			build[a++] = i;
			
			// Next operation is after this one
			i += sz;
		}
		
		// Exactly sized? (Only contains single wide ops)
		if (a == len)
			return build;
		
		// Setup a new given sized array
		int[] rv = new int[a];
		for (int x = 0; x < a; x++)
			rv[x] = build[x];
		
		// Return that instead
		return rv;
	}
	
	
	/**
	 * This determines the size of the given instruction.
	 *
	 * @param __pos The position of the instruction.
	 * @return The size of the given instruction.
	 * @throws BCException If the instruction runs off the code block
	 * or it is unknown or invalid.
	 * @since 2016/03/29
	 */
	int __sizeOf(int __pos)
		throws BCException
	{
		// Get the buffer
		CIByteBuffer buffer = this.buffer;
		int len = buffer.length();
		
		// Could be out of bounds
		int opcode = -1;
		try
		{
			// Read the opcode
			opcode = buffer.readUnsignedByte(__pos);
			
			// Wide operations
			if (opcode == 196)
			{
				// Read a new byte
				opcode = buffer.readUnsignedByte(__pos, 1);
				
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
				int lo = buffer.readInt(ppos, 4);
				int hi = buffer.readInt(ppos, 8);
				
				// Lower must really be lower
				// {@squirreljme.error AX01 The {@code tableswitch}
				// operation has a low value which is higher than the high
				// value. (The position of the current operation; The
				// low byte; The high byte)}
				if (lo > hi)
					throw new BCException(String.format("AX01 %d %d %d", __pos,
						lo, hi));
				
				// Calculate the size
				return (ppos - __pos) + 12 + (4 * ((hi - lo) + 1));
			}
			
			// Lookup switch
			else if (opcode == 171)
			{
				// Get padded position, which is rounded up to the next byte
				int ppos = ((__pos + 3) & (~3));
				
				// Read the pair count
				int np = buffer.readInt(ppos, 4);
				
				// {@squirreljme.error AX02 The {@code lookupswitch} operation
				// has a zero or negative pair count. (The position of the
				// current operation; The pair count)}
				if (np <= 0)
					throw new BCException(String.format("AX02 %d %d", __pos,
						np));
				
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

			// {@squirreljme.error AX07 Method byte code contains an illegal
			// opcode. (The opcode)}
			throw new BCException(String.format("AX07 %d", opcode));
		}
		
		// Out of bounds instruction
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error AX03 While decoding an operation, the bounds
			// of the program were exceeded. (The current opcode)}
			throw new BCException(String.format("AX03 %d", opcode), e);
		}
	}
}

