// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This represents a single sequence which is used to determine how an ELF is
 * to be written.
 *
 * @since 2016/08/16
 */
abstract class __Sequence__
{
	/**
	 * Makes the write sequence list.
	 *
	 * @param __eo The output ELF to sequence.
	 * @return The write sequence.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	static List<__Sequence__> __makeSequences(ELFOutput __eo)
		throws NullPointerException
	{
		// Check
		if (__eo == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

