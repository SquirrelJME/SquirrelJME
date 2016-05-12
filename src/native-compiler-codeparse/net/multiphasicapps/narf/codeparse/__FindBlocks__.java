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

import java.util.Map;
import net.multiphasicapps.narf.bytecode.NBCByteCode;
import net.multiphasicapps.narf.bytecode.NBCOperation;

/**
 * This finds basic blocks in the program.
 *
 * @since 2016/05/12
 */
class __FindBlocks__
{
	/**
	 * Initializes the block finder.
	 *
	 * @param __cp The program to parse.
	 * @param __bc The byte code representation.
	 * @param __bl The output block map.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	__FindBlocks__(NCPCodeParser __cp, NBCByteCode __bc,
		Map<Integer, __Block__> __bl)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __bc == null || __bl == null)
			throw new NullPointerException("NARG");
		
		// Go through all operations
		int n = __bc.size();
		for (int i = 0; i < n; i++)
		{
			NBCOperation op = __bc.get(i); 	
			
			throw new Error("TODO");
		}
	}
}

