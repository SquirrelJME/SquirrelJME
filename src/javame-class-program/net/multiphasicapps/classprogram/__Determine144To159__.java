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
}

