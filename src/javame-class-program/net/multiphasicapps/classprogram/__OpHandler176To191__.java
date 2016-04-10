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
	 * @param __op Current operation.
	 * @since 2016/04/09
	 */
	void __new(CPComputeMachine<? extends Object> __cm, Object __a, CPOp __op)
	{
		throw new Error("TODO");
	}
}

