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
 * Determines the stack operations for opcodes 176 to 191.
 *
 * @since 2016/04/18
 */
class __Determine176To191__
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
				// New
			case 187:
				__new(__dt, __op);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Calculates the new operation.
	 *
	 * @param __dt The owning determiner
	 * @param __op The current operation.
	 * @since 2016/04/11
	 */
	private void __new(__DetermineTypes__ __dt, CPOp __op)
	{
		// Just add an element to the stack
		__dt.operate(__op, null, null, CPVariableType.OBJECT);
	}
}

