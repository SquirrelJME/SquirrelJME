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

import net.multiphasicapps.classfile.CFClassName;
import net.multiphasicapps.classfile.CFConstantEntry;
import net.multiphasicapps.classfile.CFConstantPool;
import net.multiphasicapps.classfile.CFFieldReference;
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
				// getstatic
			case 178:
				__getstatic(__cm, __a, __op);
				break;
				
				// invokevirtual
			case 182:
				__invoke(__cm, __a, __op, CPInvokeType.VIRTUAL);
				break;
				
				// invokespecial
			case 183:
				__invoke(__cm, __a, __op, CPInvokeType.SPECIAL);
				break;
			
				// invokestatic
			case 184:
				__invoke(__cm, __a, __op, CPInvokeType.STATIC);
				break;
				
				// invokeinterface
			case 185:
				__invoke(__cm, __a, __op, CPInvokeType.INTERFACE);
				break;
			
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
	 * Obtains the value of a static variable.
	 *
	 * @param __cm Compute machine.
	 * @param __a Passed value.
	 * @param __op The operation.
	 * @since 2016/04/15
	 */
	void __getstatic(CPComputeMachine<? extends Object> __cm, Object __a,
		CPOp __op)
	{
		// Place value onto the stack
		__castCM(__cm).getStaticField(__a, __op.variables().getStackTop(),
			((CFFieldReference)__op.arguments().get(0)));
	}
	
	/**
	 * Invokes a method.
	 *
	 * @param __cm Compute machine.
	 * @param __a Passed value.
	 * @param __op The operation.
	 * @param __it The operation to be performed.
	 * @since 2016/04/15
	 */
	void __invoke(CPComputeMachine<? extends Object> __cm, Object __a,
		CPOp __op, CPInvokeType __it)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __cm The compute machine.
	 * @param __a Passed A.
	 * @param __op Current operation.
	 * @since 2016/04/09
	 */
	void __new(CPComputeMachine<? extends Object> __cm, Object __a, CPOp __op)
	{
		// Perform the allocation
		__castCM(__cm).allocateObject(__a, __op.variables().getStackTop(),
			((ClassNameSymbol)__op.arguments().get(0)));
	}
}

