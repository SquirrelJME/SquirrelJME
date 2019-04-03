// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * This represents a math operation to be performed.
 *
 * @since 2019/04/03
 */
public enum MathOperationType
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
	
	/** End. */
	;
}

