// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

import net.multiphasicapps.squirreljme.ci.CIByteBuffer;

/**
 * This represents a raw byte code operation, it only contains integral field
 * values which indicate the operation that is performed along with its
 * arguments.
 *
 * @since 2016/06/22
 */
public final class BCRawOperation
{
	/** The owning byte code. */
	protected final BCByteCode owner;
	
	/** The logical address of this instruction. */
	protected final int logicaladdress;
	
	/**
	 * Initializes the raw operation.
	 *
	 * @param __bc The owning byte code.
	 * @param __bb The code buffer.
	 * @param __lp The logical address of this instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/22
	 */
	BCRawOperation(BCByteCode __bc, CIByteBuffer __bb, int __lp)
		throws NullPointerException
	{
		if (__bc == null || __bb == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __bc;
		this.logicaladdress = __lp;
		
		throw new Error("TODO");
	}
}

