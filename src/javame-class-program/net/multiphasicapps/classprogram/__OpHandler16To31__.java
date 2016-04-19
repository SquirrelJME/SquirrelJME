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

import net.multiphasicapps.classfile.CFConstantString;
import net.multiphasicapps.classfile.CFConstantValue;

/**
 * Handles operations 16 to 31.
 *
 * @since 2016/04/19
 */
class __OpHandler16To31__
	extends __VMWorkers__.__Worker__
{
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
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
				// ldc
			case 18:
			case 19:
			case 20:
				__ldc_x(__cm, __a, __op);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Loads a constant pool entry onto the stack.
	 *
	 * @param __cm The compute machine.
	 * @param __a Passed A.
	 * @param __op Current operation.
	 * @since 2016/04/19
	 */
	static void __ldc_x(CPComputeMachine<? extends Object> __cm, Object __a,
		CPOp __op)
	{
		// Get the pool entry here
		CFConstantValue cv = ((CFConstantValue)__op.arguments().get(0));
		
		// Set topmost stack item to the given constant
		if (cv instanceof CFConstantString)
			__castCM(__cm).setConstant(__a, __op.variables().getStackTop(),
				((CFConstantString)cv).toString());
		
		// Unknown
		else
			throw new RuntimeException("WTFX");
	}
}

