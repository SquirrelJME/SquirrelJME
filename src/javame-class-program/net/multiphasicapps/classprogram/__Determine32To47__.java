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
 * Determines the stack operations for opcodes 32 to 57.
 *
 * @since 2016/04/18
 */
class __Determine32To47__
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
				// Load long
			case 32:
			case 33:
				__load_n(__dt, __op, opcode - 30, CPVariableType.LONG);
				break;
			
				// Load float
			case 34:
			case 35:
			case 36:
			case 37:
				__load_n(__dt, __op, opcode - 34, CPVariableType.FLOAT);
				break;
			
				// Load double
			case 38:
			case 39:
			case 40:
			case 41:
				__load_n(__dt, __op, opcode - 38, CPVariableType.DOUBLE);
				break;
				
				// Load object
			case 42:
			case 43:
			case 44:
			case 45:
				__load_n(__dt, __op, opcode - 42, CPVariableType.OBJECT);
				break;
				
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Load variable from locals and push it onto the stack.
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @param __dx The index.
	 * @param __t The type of value to load.
	 * @since 2016/04/12
	 */
	static void __load_n(__DetermineTypes__ __dt, CPOp __op, int __dx,
		CPVariableType __t)
	{
		// Load it onto the stack
		__dt.operate(__op, -__dx, __t, null, null, __t);
	}
	
	/**
	 * Load variable from locals and push it onto the stack (wide).
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @param __t The type of value to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	static void __load_w(__DetermineTypes__ __dt, CPOp __op,
		CPVariableType __t)
	{
		__load_n(__dt, __op, ((Number)__op.arguments().get(0)).intValue(),
			__t);
	}
}

