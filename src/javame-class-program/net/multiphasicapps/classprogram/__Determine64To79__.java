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
 * Determines the stack operations for opcodes 64 to 79.
 *
 * @since 2016/04/18
 */
class __Determine64To79__
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
				// Store long
			case 64:
			case 65:
			case 66:
				__store_n(__dt, __op, opcode - 63, CPVariableType.LONG);
				break;
			
				// Store float
			case 67:
			case 68:
			case 69:
			case 70:
				__store_n(__dt, __op, opcode - 67, CPVariableType.FLOAT);
				break;
			
				// Store double
			case 71:
			case 72:
			case 73:
			case 74:
				__store_n(__dt, __op, opcode - 71, CPVariableType.DOUBLE);
				break;
			
				// Store object
			case 75:
			case 76:
			case 77:
			case 78:
				__store_n(__dt, __op, opcode - 75, CPVariableType.OBJECT);
				break;
				
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Store variable from the stack and place it in a local.
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @param __dx The index.
	 * @param __t The type of value to store.
	 * @since 2016/04/14
	 */
	static void __store_n(__DetermineTypes__ __dt, CPOp __op, int __dx,
		CPVariableType __t)
	{
		__dt.operate(__op, __dx, __t, null, __t, null);
	}
}

