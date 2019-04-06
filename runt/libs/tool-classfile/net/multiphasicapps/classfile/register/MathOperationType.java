// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.PrimitiveType;

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
	
	/**
	 * Returns the register instruction used for the operation.
	 *
	 * @param __pt The primitive type.
	 * @return The operation to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	public final int operation(DataType __pt)
		throws NullPointerException
	{
		switch (__pt)
		{
			case INTEGER:
				switch (this)
				{
					case ADD:	return RegisterOperationType.INT_ADD;
					case SUB:	return RegisterOperationType.INT_SUB;
					case MUL:	return RegisterOperationType.INT_MUL;
					case DIV:	return RegisterOperationType.INT_DIV;
					case REM:	return RegisterOperationType.INT_REM;
					case SHL:	return RegisterOperationType.INT_SHL;
					case SHR:	return RegisterOperationType.INT_SHR;
					case USHR:	return RegisterOperationType.INT_USHR;
					case AND:	return RegisterOperationType.INT_AND;
					case OR:	return RegisterOperationType.INT_OR;
					case XOR:	return RegisterOperationType.INT_XOR;
				}
				break;
			
			case LONG:
				switch (this)
				{
					case ADD:	return RegisterOperationType.LONG_ADD;
					case SUB:	return RegisterOperationType.LONG_SUB;
					case MUL:	return RegisterOperationType.LONG_MUL;
					case DIV:	return RegisterOperationType.LONG_DIV;
					case REM:	return RegisterOperationType.LONG_REM;
					case SHL:	return RegisterOperationType.LONG_SHL;
					case SHR:	return RegisterOperationType.LONG_SHR;
					case USHR:	return RegisterOperationType.LONG_USHR;
					case AND:	return RegisterOperationType.LONG_AND;
					case OR:	return RegisterOperationType.LONG_OR;
					case XOR:	return RegisterOperationType.LONG_XOR;
				}
				break;
			
			case FLOAT:
				switch (this)
				{
					case ADD:	return RegisterOperationType.FLOAT_ADD;
					case SUB:	return RegisterOperationType.FLOAT_SUB;
					case MUL:	return RegisterOperationType.FLOAT_MUL;
					case DIV:	return RegisterOperationType.FLOAT_DIV;
					case REM:	return RegisterOperationType.FLOAT_REM;
				}
				break;
			
			case DOUBLE:
				switch (this)
				{
					case ADD:	return RegisterOperationType.DOUBLE_ADD;
					case SUB:	return RegisterOperationType.DOUBLE_SUB;
					case MUL:	return RegisterOperationType.DOUBLE_MUL;
					case DIV:	return RegisterOperationType.DOUBLE_DIV;
					case REM:	return RegisterOperationType.DOUBLE_REM;
				}
				break;
		}
		
		throw new todo.OOPS(this.name());
	}
}

