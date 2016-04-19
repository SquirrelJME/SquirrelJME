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
 * Determines the stack operations for opcodes 48 to 63.
 *
 * @since 2016/04/18
 */
class __Determine48To63__
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
				// Store integer
			case 54:
				__store(__dt, __op, CPVariableType.INTEGER);
				break;
				
				// Store long
			case 55:
				__store(__dt, __op, CPVariableType.LONG);
				break;
				
				// Store float
			case 56:
				__store(__dt, __op, CPVariableType.FLOAT);
				break;
				
				// Store double
			case 57:
				__store(__dt, __op, CPVariableType.DOUBLE);
				break;
				
				// Store object
			case 58:
				__store(__dt, __op, CPVariableType.OBJECT);
				break;
				
				// Store integer
			case 59:
			case 60:
			case 61:
			case 62:
				__Determine64To79__.__store_n(__dt, __op, opcode - 59,
					CPVariableType.INTEGER);
				break;
			
				// Store long
			case 63:
				__Determine64To79__.__store_n(__dt, __op, opcode - 63,
					CPVariableType.LONG);
				break;
			
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Store variable from the stack and place it in a local (narrow).
	 *
	 * @param __dt The determiner.
	 * @param __op The input operation.
	 * @param __t The type of value to store.
	 * @since 2016/04/14
	 */
	static void __store(__DetermineTypes__ __dt, CPOp __op,
		CPVariableType __t)
	{
		__Determine64To79__.__store_n(__dt, __op,
			((Number)__op.arguments().get(0)).intValue(), __t);
	}
}

