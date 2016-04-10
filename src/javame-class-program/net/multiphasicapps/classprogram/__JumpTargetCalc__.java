// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This class contains a static which is used to calculate the jump targets of
 * and instruction that are not of the exception kind.
 *
 * Most jumps naturally flow to the next instruction, while others jump to
 * specific instructions.
 *
 * @since 2016/04/10
 */
class __JumpTargetCalc__
{
	/**
	 * Not used.
	 *
	 * @since 2016/04/10
	 */
	private __JumpTargetCalc__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Calculates the jump target for the given instruction.
	 *
	 * @param __oc The opcode of this instruction.
	 * @param __code The block of code.
	 * @param __paddr The physical address of this instruction.
	 * @return The array of jump targets (in physical addresses).
	 * @throws CPProgramException If a jump target is not valid.
	 * @since 2016/04/10
	 */
	static int[] __calculate(int __oc, byte[] __code, int __paddr)
		throws CPProgramException
	{
		// The following have no jump targets
		if (__oc == CPOpcodes.ARETURN ||
			__oc == CPOpcodes.ATHROW ||
			__oc == CPOpcodes.DRETURN ||
			__oc == CPOpcodes.FRETURN ||
			__oc == CPOpcodes.IRETURN ||
			__oc == CPOpcodes.LRETURN ||
			__oc == CPOpcodes.RETURN)
			return new int[0];
	
		// Lookup switch
		else if (__oc == CPOpcodes.LOOKUPSWITCH)
		{
			// Align pointer to read the jump values
			int ap = ((__paddr + 3) & (~3));
		
			// Read the default offset
			int defo = __ByteUtils__.__readSInt(__code, ap);
		
			// Read the pair count
			int np = __ByteUtils__.__readSInt(__code, ap + 4);
		
			// There are a specific number of pairs so an array can
			// be used
			int[] rv = new int[np + 1];
			
			// Setup default first
			rv[0] = __paddr + defo;
			
			// Add extra value pairs now
			int lastdx = Integer.MIN_VALUE;
			boolean lastisval = false;
			for (int j = 0; j < np; j++)
			{
				// Read the key here
				int baseoff = ap + 8 + (j * 8);
				int keyv = __ByteUtils__.__readSInt(__code, baseoff);
			
				// It MUST be higher than the last index, that is all
				// entries in the switch are sorted.
				// {@squirreljme.error CP0a Subsequent keys in the
				// {@link lookupswitch} operation must have higher
				// valued keys placed higher so that they may be
				// binary searched. (The physical address)}
				if (keyv < lastdx && lastisval)
					throw new CPProgramException(String.format(
						"CP0a %s", __paddr));
			
				// Set the last value which is checked to make sure the
				// key is actually higher
				lastdx = keyv;
				lastisval = true;
			
				// Read the offset and calculate the jump
				// {@squirreljme.error CP0q Lookupswitch jump outside of the
				// program bounds.}
				int joff = __ByteUtils__.__readSInt(__code, baseoff + 4);
				if ((rv[1 + j] = __paddr + joff) == Integer.MIN_VALUE)
					throw new CPProgramException("CP0q");
			}
			
			// Return it
			return rv;
		}
	
		// Table switch
		else if (__oc == CPOpcodes.TABLESWITCH)
		{
			// Align pointer to read the jump values
			int ap = ((__paddr + 3) & (~3));
			
			// Read the default offset
			int defo = __ByteUtils__.__readSInt(__code, ap);
			
			// Read the low to high values
			int lo = __ByteUtils__.__readSInt(__code, ap + 4);
			int hi = __ByteUtils__.__readSInt(__code, ap + 8);
			
			// The number of items
			int ni = (hi - lo) + 1;
			
			// Setup the target array
			int num = ni + 1;
			int[] rv = new int[num];
			
			// Default jump
			rv[0] = __paddr + defo;
			
			// Read all the other offsets in
			// {@squirreljme.error CP0p Table switch jump out of program
			// bounds}.
			int baseoff = ap + 12;
			for (int i = 0; i < ni; i++)
				if ((rv[1 + i] = __paddr + __ByteUtils__.__readSInt(__code,
					baseoff + (4 * i))) == Integer.MIN_VALUE)
					throw new CPProgramException("CP0p");
			
			// Return it
			return rv;
		}
	
		// Goto a single address (16-bit)
		else if (__oc == CPOpcodes.GOTO)
			return new int[]{__paddr + 
				__ByteUtils__.__readSShort(__code, __paddr + 1)};
	
		// Goto a single address (32-bit)
		else if (__oc == CPOpcodes.GOTO_W)
		{
			// {@squirreljme.error CP0r 32-bit jump outside of the program
			// bounds.}
			int addr = __paddr +
				__ByteUtils__.__readSInt(__code, __paddr + 1);
			if (addr == Integer.MIN_VALUE)
				throw new CPProgramException("CP0r");
			
			// Return it
			return new int[]{addr};
		}
	
		// Conditional to a given instruction or if false, the next
		// instruction.
		else if (__oc == CPOpcodes.IF_ACMPEQ ||
			__oc == CPOpcodes.IF_ACMPNE ||
			__oc == CPOpcodes.IFEQ ||
			__oc == CPOpcodes.IFGE ||
			__oc == CPOpcodes.IFGT ||
			__oc == CPOpcodes.IF_ICMPEQ ||
			__oc == CPOpcodes.IF_ICMPGE ||
			__oc == CPOpcodes.IF_ICMPGT ||
			__oc == CPOpcodes.IF_ICMPLE ||
			__oc == CPOpcodes.IF_ICMPLT ||
			__oc == CPOpcodes.IF_ICMPNE ||
			__oc == CPOpcodes.IFLE ||
			__oc == CPOpcodes.IFLT ||
			__oc == CPOpcodes.IFNE ||
			__oc == CPOpcodes.IFNONNULL ||
			__oc == CPOpcodes.IFNULL)
			return new int[]{Integer.MIN_VALUE, __paddr +
				__ByteUtils__.__readUShort(__code, __paddr + 1)};
	
		// Implicit next instruction
		else
			return new int[]{Integer.MIN_VALUE};
	}
}

