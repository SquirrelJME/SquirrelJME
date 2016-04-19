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
 * Determines the stack operations for opcodes 144 to 159.
 *
 * @since 2016/04/19
 */
class __Determine144To159__
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
				// Compare long
			case 148:
				__iftwo(__dt, __op, CPVariableType.LONG);
				break;
			
				// Compare float
			case 149:
			case 150:
				__iftwo(__dt, __op, CPVariableType.FLOAT);
				break;
				
				// Compare double
			case 151:
			case 152:
				__iftwo(__dt, __op, CPVariableType.DOUBLE);
				break;
			
				// Compare int against zero
			case 153:
			case 154:
			case 155:
			case 156:
			case 157:
			case 158:
				__ifint(__dt, __op);
				break;
				
				// Unknown
			default:
				throw new __VMWorkers__.__UnknownOp__();
		}
	}
	
	/**
	 * Compare integer against zero.
	 *
	 * @param __dt The determiner.
	 * @param __op The current operation.
	 * @since 2016/04/19
	 */
	static void __ifint(__DetermineTypes__ __dt, CPOp __op)
	{
		__dt.operate(__op, null, CPVariableType.INTEGER, null);
	}
	
	/**
	 * Compare two values and return a signum.
	 *
	 * @param __dt The determiner.
	 * @param __op The current operation.
	 * @param __t The types to compare.
	 * @since 2016/04/19
	 */
	static void __iftwo(__DetermineTypes__ __dt, CPOp __op, CPVariableType __t)
	{
		__dt.operate(__op, null, __t, __t, null, CPVariableType.INTEGER);
	}
}

