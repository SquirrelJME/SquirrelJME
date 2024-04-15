// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * Represents the type of argument that an instruction may have.
 *
 * @since 2024/01/30
 */
public enum InstructionRawArgumentType
{
	/** Zero byte padding. */
	PADDING_0,
	
	/** One byte padding. */
	PADDING_1,
	
	/** Two byte padding. */
	PADDING_2,
	
	/** Three byte padding. */
	PADDING_3,
	
	/** Signed byte. */
	SIGNED_BYTE,
	
	/** Unsigned byte. */
	UNSIGNED_BYTE,
	
	/** Signed short. */
	SIGNED_SHORT,
	
	/** Unsigned short. */
	UNSIGNED_SHORT,
	
	/** Jump-short value. */
	JUMP_SHORT,
	
	/** Integer. */
	INTEGER,
	
	/** Jump-integer value. */
	JUMP_INTEGER,
	
	/** Lookup switch table. */
	LOOKUPSWITCH,
	
	/** Table switch table. */
	TABLESWITCH,
	
	/* End. */
	;
	
	/**
	 * Returns the padding argument to use.
	 *
	 * @param __i The padding to get.
	 * @return The resultant padding instruction.
	 * @throws InvalidClassFormatException If the padding is not valid.
	 * @since 2024/01/30
	 */
	public static InstructionRawArgumentType padding(int __i)
		throws InvalidClassFormatException
	{
		switch (__i)
		{
			case 0: return InstructionRawArgumentType.PADDING_0;
			case 1: return InstructionRawArgumentType.PADDING_1;
			case 2: return InstructionRawArgumentType.PADDING_2;
			case 3: return InstructionRawArgumentType.PADDING_3;
		}
		
		throw new InvalidClassFormatException("IOOB " + __i);
	}
}
