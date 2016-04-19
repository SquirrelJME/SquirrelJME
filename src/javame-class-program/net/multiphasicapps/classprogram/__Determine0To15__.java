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
 * Determines the stack operations for opcodes 0 to 15.
 *
 * @since 2016/04/19
 */
class __Determine0To15__
	extends __VMWorkers__.__Determiner__
{
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
	 */
	@Override
	public void determine(__DetermineTypes__ __dt, CPOp __op)
	{
		// Depends on the opcode
		int opcode = __op.instructionCode();
		switch (opcode)
		{
				// Do nothing
			case 0:
				break;
				
				// null constant
			case 1:
				__const(__dt, __op, CPVariableType.OBJECT);
				break;
				
				// Integer constants
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				__const(__dt, __op, CPVariableType.INTEGER);
				break;
				
				// Long constants
			case 9:
			case 10:
				__const(__dt, __op, CPVariableType.LONG);
				break;
				
				// Float constants
			case 11:
			case 12:
			case 13:
				__const(__dt, __op, CPVariableType.FLOAT);
				break;
				
				// Double constants
			case 14:
			case 15:
				__const(__dt, __op, CPVariableType.DOUBLE);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Pushes a constant to the stack.
	 *
	 * @param __dt The type determiner.
	 * @param __op The operation to perform.
	 * @param __t The variable type.
	 * @since 2016/04/19
	 */
	static void __const(__DetermineTypes__ __dt, CPOp __op, CPVariableType __t)
	{
		__dt.operate(__op, null, null, __t);
	}
}

