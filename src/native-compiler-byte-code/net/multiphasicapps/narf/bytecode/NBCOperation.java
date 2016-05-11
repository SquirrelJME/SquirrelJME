// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

/**
 * This represents a single operation in the byte code.
 *
 * @since 2016/05/11
 */
public final class NBCOperation
{
	/** The owning byte code. */
	protected final NBCByteCode owner;
	
	/** The logical position. */
	protected final int logicaladdress;
	
	/**
	 * Initializes the operation data.
	 *
	 * @param __bc The owning byte code.
	 * @param __lp The logical position of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/11
	 */
	NBCOperation(NBCByteCode __bc, int __lp)
		throws NullPointerException
	{
		// Check
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __bc;
		logicaladdress = __lp;
	}
}

