// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf;

/**
 * This represents a NARF operation, this is the code which is executed when it
 * is ran.
 *
 * @since 2016/03/21
 */
public enum NARFOpCode
{
	/** End. */
	;
	
	/** This is the value mask which opcodes must be contained within. */
	public static final int OPCODE_VALUE_MASK =
		0b11111;
	
	/** The operation value. */
	protected final int opcode;
	
	/** The validity mask for slot A. */
	protected final int validitya;
	
	/** The validity mask for slot B. */
	protected final int validityb;
	
	/**
	 * Initializes the operation.
	 *
	 * @param __b The operation identifier.
	 * @param __vma The validity mask for slot A.
	 * @param __vmb The validity mask for slot B.
	 * @throws IllegalArgumentException If the bits have unknown bits set; if
	 * the validity masks have unknown bits.
	 * @since 2016/03/21
	 */
	private NARFOpCode(int __b, int __vma, int __vmb)
		throws IllegalArgumentException
	{
		// Unknown bits set?
		if (0 != (__b & (~OPCODE_VALUE_MASK)))
			throw new IllegalArgumentException(String.format("NF01 %x", __b));
		
		// Unknown validity masks?
		if (0 != ((__vma | __vmb) & (~NARFRegisterType.VALIDITY_ALL)))
			throw new IllegalArgumentException(String.format("NF03 %x %x",
				__vma, __vmb));
		
		// Set
		opcode = __b;
		validitya = __vma;
		validityb = __vmb;
	}
	
	/**
	 * Checks if the given register types are valid for this instruction.
	 *
	 * @param __at The slot A type.
	 * @param __bt The slot B type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public boolean isValidFor(NARFRegisterType __at, NARFRegisterType __bt)
		throws NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Check the mask to see if this type if valid for the given operation
		return (0 != (validitya & __at.validityFlag())) &&
			(0 != (validityb & __bt.validityFlag()));
	}
	
	/**
	 * Returns the opcode.
	 *
	 * @return The opcode.
	 * @since 2016/03/21
	 */
	public int opCode()
	{
		return opcode;
	}
	
	/**
	 * Returns the validity mask for the given slot.
	 *
	 * @param __sl The slot to get the validity mask for.
	 * @return The validity mask for the given slot.
	 * @throws illegalArgumentException If the slot is unknown.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/21
	 */
	public int validityMask(NARFSlot __sl)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__sl == null)
			throw new NullPointerException("NARF");
		
		// Depends
		switch (__sl)
		{
				// A
			case A:
				return validitya;
				
				// B
			case B:
				return validityb;
				
				// Unknown
			default:
				throw new IllegalArgumentException(String.format("NF02 %s",
					__sl));
		}
	}
}

