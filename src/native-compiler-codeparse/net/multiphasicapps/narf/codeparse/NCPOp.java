// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This describes an operation.
 *
 * @since 2016/05/10
 */
public final class NCPOp
{
	/**
	 * Initializes the operation data.
	 *
	 * @param __cp The owning code parser.
	 * @param __code The code data.
	 * @param __pa The physical address in the code buffer.
	 * @throws NCPException If the operation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/10
	 */
	NCPOp(NCPCodeParser __cp, NCIByteBuffer __code, int __pa)
		throws NCPException, NullPointerException
	{
		// Check
		if (__cp == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Read the opcode
		int opcode = __code.readUnsignedByte(__pa);
		if (opcode == NCPOpCode.WIDE)
			opcode = (NCPOpCode.WIDE << 8) | __code.readUnsignedByte(__pa, 1);
		
		System.err.printf("DEBUG -- Op %d%n", opcode);
		
		throw new Error("TODO");
	}
}

