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
	SIGN_X8,
	
	/** Sign half (int = x16, long = x32). */
	SIGN_HALF,
	
	/** End. */
	;
}

