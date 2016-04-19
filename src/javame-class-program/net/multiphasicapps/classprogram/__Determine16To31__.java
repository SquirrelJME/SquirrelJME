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

import net.multiphasicapps.classfile.CFConstantDouble;
import net.multiphasicapps.classfile.CFConstantFloat;
import net.multiphasicapps.classfile.CFConstantInteger;
import net.multiphasicapps.classfile.CFConstantLong;
import net.multiphasicapps.classfile.CFConstantString;
import net.multiphasicapps.classfile.CFConstantValue;

/**
 * Determines the stack operations for opcodes 16 to 31.
 *
 * @since 2016/04/18
 */
class __Determine16To31__
	extends __VMWorkers__.__Determiner__
{
	/**
	 * {@inheritDoc}
	 * @since 2016/04/18
	 */
	@Override
	public void determine(__DetermineTypes__ __dt, CPOp __op)
	{
		// Depends on the opcode
		int opcode = __op.instructionCode();
		switch (opcode)
		{
				// Push byte or short
			case 16:
			case 17:
				__bsipush(__dt, __op);
				break;
			
				// Load pool constant (narrow byte/short)
			case 18:
			case 19:
				__ldc(__dt, __op);
				break;
				
				// Load pool constant (wide short)
			case 20:
				__ldc2_w(__dt, __op);
				break;
			
				// Load integer
			case 21:
				__load(__dt, __op, CPVariableType.INTEGER);
				break;
				
				// Load long
			case 22:
				__load(__dt, __op, CPVariableType.LONG);
				break;
				
				// Load float
			case 23:
				__load(__dt, __op, CPVariableType.FLOAT);
				break;
				
				// Load double
			case 24:
				__load(__dt, __op, CPVariableType.DOUBLE);
					break;
				
				// Load object
			case 25:
				__load(__dt, __op, CPVariableType.OBJECT);
				break;
			
				// Load integer
			case 26:
			case 27:
			case 28:
			case 29:
				__Determine32To47__.__load_n(__dt, __op, opcode - 26,
					CPVariableType.INTEGER);
				break;
			
				// Load long
			case 30:
			case 31:
				__Determine32To47__.__load_n(__dt, __op, opcode - 30,
					CPVariableType.LONG);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Pushes a byte or short to the stack.
	 *
	 * @param __dt Type determiner.
	 * @param __op The operation.
	 * @since 2016/04/19
	 */
	static void __bsipush(__DetermineTypes__ __dt, CPOp __op)
	{
		__dt.operate(__op, null, null, CPVariableType.INTEGER);
	}
	
	/**
	 * Loads a narrow constant pool entry.
	 *
	 * @param __dt Type determiner.
	 * @param __op The operation.
	 * @since 2016/04/18
	 */
	static void __ldc(__DetermineTypes__ __dt, CPOp __op)
	{
		__ldc_x(__dt, __op, false);
	}
	
	/**
	 * Performs loading of a constant pool entry.
	 *
	 * @param __dt Type determiner.
	 * @param __op The operation.
	 * @param __wide Must the operation be wide?
	 * @since 2016/04/18
	 */
	static void __ldc_x(__DetermineTypes__ __dt, CPOp __op, boolean __wide)
	{
		// Get the pool entry here
		CFConstantValue cv = ((CFConstantValue)__op.arguments().get(0));
		
		// {@squirreljme.error CP1h Cannot load the given constant pool entry
		// onto the stack because it does not match the expected wideness of
		// the instruction. (The operation address; The constant value)}
		if (__wide != cv.isWide())
			throw new CPProgramException(String.format("CP1h %d %s",
				__op.address(), cv));
		
		// String
		if (cv instanceof CFConstantString)
			__dt.operate(__op, null, null, CPVariableType.OBJECT);
		
		// Integer
		else if (cv instanceof CFConstantInteger)
			__dt.operate(__op, null, null, CPVariableType.INTEGER);
		
		// Long
		else if (cv instanceof CFConstantLong)
			__dt.operate(__op, null, null, CPVariableType.LONG);
		
		// Float
		else if (cv instanceof CFConstantFloat)
			__dt.operate(__op, null, null, CPVariableType.FLOAT);
			
		// Double
		else if (cv instanceof CFConstantDouble)
			__dt.operate(__op, null, null, CPVariableType.DOUBLE);
			
		// Unknown
		else
			throw new RuntimeException("WTFX");
	}
	
	/**
	 * Loads a wide constant pool entry (can access entire pool).
	 *
	 * @param __dt Type determiner.
	 * @param __op The operation.
	 * @since 2016/04/18
	 */
	static void __ldc2_w(__DetermineTypes__ __dt, CPOp __op)
	{
		__ldc_x(__dt, __op, true);
	}
	
	/**
	 * Load variable from locals and push it onto the stack.
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	static void __load(__DetermineTypes__ __dt, CPOp __op, CPVariableType __t)
	{
		__Determine32To47__.__load_n(__dt, __op,
			((Number)__op.arguments().get(0)).intValue(), __t);
	}
}

