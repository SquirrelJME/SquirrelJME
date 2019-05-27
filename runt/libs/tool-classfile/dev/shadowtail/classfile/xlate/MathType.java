// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.PrimitiveType;

/**
 * This represents a math operation to be performed.
 *
 * @since 2019/04/03
 */
public enum MathType
{
	/** Add. */
	ADD,
	
	/** Subtract. */
	SUB,
	
	/** Multiply. */
	MUL,
	
	/** Divide. */
	DIV,
	
	/** Remainder. */
	REM,
	
	/** Negate. */
	NEG,
	
	/** Shift left. */
	SHL,
	
	/** Shift right. */
	SHR,
	
	/** Unsigned shift right. */
	USHR,
	
	/** And. */
	AND,
	
	/** Or. */
	OR,
	
	/** Xor. */
	XOR,
	
	/** Compare (float compare less). */
	CMPL,
	
	/** Compare (float compare greater). */
	CMPG,
	
	/** Sign 8-bit value. */
	SIGNX8,
	
	/** Sign 16-bit value. */
	SIGNX16,
	
	/** End. */
	;
	
	/**
	 * Returns the math type for the given index.
	 *
	 * @param __i The index.
	 * @return The resulting math type.
	 * @since 2019/04/08
	 */
	public static final MathType of(int __i)
	{
		switch (__i)
		{
			case 0:		return ADD;
			case 1:		return SUB;
			case 2:		return MUL;
			case 3:		return DIV;
			case 4:		return REM;
			case 5:		return NEG;
			case 6:		return SHL;
			case 7:		return SHR;
			case 8:		return USHR;
			case 9:		return AND;
			case 10:	return OR;
			case 11:	return XOR;
			case 12:	return CMPL;
			case 13:	return CMPG;
			case 14:	return SIGNX8;
			case 15:	return SIGNX16;
		}
		
		// {@squirreljme.error JC39 Invalid math operation.}
		throw new IllegalArgumentException("JC39");
	}
}

