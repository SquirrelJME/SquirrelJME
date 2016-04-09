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

import net.multiphasicapps.classfile.CFConstantEntry;
import net.multiphasicapps.classfile.CFConstantPool;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * Handles opcodes 176 to 191.
 *
 * @since 2016/04/09
 */
class __OpHandler176To191__
	extends __VMWorkers__.__Worker__
{
	/**
	 * Not used.
	 *
	 * @since 2016/04/09
	 */
	__OpHandler176To191__()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/09
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
				// new
			case 187:
				__new(__cm, __a, __op);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __cm The compute machine.
	 * @param __a Passed A.
	 * @param __b Passed B.
	 * @since 2016/04/09
	 */
	void __new(CPComputeMachine<? extends Object> __cm, Object __a, CPOp __op)
	{
		// Obtain the constant pool because it has the class reference
		CFConstantPool pool = __op.__pool();
		
		// Get the referenced class entry
		ClassNameSymbol clname = pool.<CFConstantEntry.ClassName>getAs(
			__op.__readUnsignedShort(1), CFConstantEntry.ClassName.class).
			symbol();
		
		// Get the input and output variable states
		CPVariableStates inputs = __op.inputState();
		CPVariableStates outputs = __op.__outputState(false);
		boolean docalc = !outputs._gotcomputed;
		
		// Get the top of the stack
		int stacktop = inputs.getStackTop();
		
		// Calculate next state
		if (docalc)
		{
			// Push to the stack
			CPVariableState vs = outputs.__push();
			
			// Is an object
			vs.__setComputedType(CPVariableType.OBJECT);
			
			// Becomes a new variable
			vs.__setComputedValue(vs.__newValueAt(__op));
		}
		
		// Perform the allocation
		__castCM(__cm).allocateObject(__a, stacktop, clname);
	}
}

