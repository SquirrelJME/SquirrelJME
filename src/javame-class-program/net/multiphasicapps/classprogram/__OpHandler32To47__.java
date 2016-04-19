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
 * Handles operations 32 to 47.
 *
 * @since 2016/04/16
 */
class __OpHandler32To47__
	extends __VMWorkers__.__Worker__
{
	/**
	 * No initialization required.
	 *
	 * @since 2016/04/16
	 */
	__OpHandler32To47__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/16
	 */
	@Override
	void compute(CPComputeMachine<? extends Object> __cm, Object __a,
		CPOp __op)
	{
		// Determine the opcode
		int opcode = __op.instructionCode();
		
		// Depends on the operation
		switch (opcode)
		{
				// lload
			case 32:
			case 33:
				__load(__cm, __a, __op, true, (opcode - 32) + 2);
				break;
				
				// float
			case 34:
			case 35:
			case 36:
			case 37:
				__load(__cm, __a, __op, false, opcode - 34);
				break;
				
				// double
			case 38:
			case 39:
			case 40:
			case 41:
				__load(__cm, __a, __op, true, opcode - 38);
				break;
				
				// Object
			case 42:
			case 43:
			case 44:
			case 45:
				__load(__cm, __a, __op, false, opcode - 42);
				break;
				
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Loads the given value from a local onto the stack.
	 *
	 * @param __cm The compute machine.
	 * @param __a Passed A.
	 * @param __op Current operation.
	 * @param __wide Wide?
	 * @param __lv The local variable to source from.
	 * @since 2016/04/16
	 */
	static void __load(CPComputeMachine<? extends Object> __cm, Object __a,
		CPOp __op, boolean __wide, int __lv)
	{
		// Copy up
		__castCM(__cm).copy(__a, __op.variables().getStackTop(), __lv);
	}
}

