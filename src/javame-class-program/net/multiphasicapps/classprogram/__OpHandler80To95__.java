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
 * Handles opcodes 80 to 95.
 *
 * @since 2016/04/10
 */
class __OpHandler80To95__
	extends __VMWorkers__.__Worker__
{
	/**
	 * Not used.
	 *
	 * @since 2016/04/10
	 */
	__OpHandler80To95__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
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
				// dup
			case 89:
				__dup(__cm, __a, __op);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Duplicates the topmost entry on the stack.
	 *
	 * @param __cm The compute machine.
	 * @param __a Passed A.
	 * @param __op Current operation.
	 * @since 2016/04/10
	 */
	private void __dup(CPComputeMachine<? extends Object> __cm, Object __a,
		CPOp __op)
	{
		// Get the output variable state
		CPVariableStates outputs = __op.__outputState(false);
		boolean docalc = !outputs._gotcomputed;
		
		// Calculate next state
		if (docalc)
		{
			// Derive state from the input
			CPVariableStates inputs = __op.inputState();
			
			// {@squirreljme.error CP0s Cannot duplicate topmost stack entry
			// because the stack is empty.}
			int stacktop = inputs.getStackTop();
			if (stacktop <= __op.program().maxLocals())
				throw new CPProgramException("CP0s");
			
			// Get the input variable at the current stack top
			CPVariableState ontop = inputs.get(stacktop - 1);
			CPVariableType toptype = ontop.getType();
			
			// {@squirreljme.error CP0t Cannot duplicate TOP, LONG, or DOUBLE
			// types. (The topmost type)}
			if (toptype.isWide() || toptype == CPVariableType.TOP)
				throw new CPProgramException(String.format("CP0t %s",
					toptype));
			
			// Push to the stack
			CPVariableState vs = outputs.__push();
			
			// Clone date
			vs.__setComputedType(toptype);
			vs.__setComputedValue(ontop.getValue());
		}
	}
}

