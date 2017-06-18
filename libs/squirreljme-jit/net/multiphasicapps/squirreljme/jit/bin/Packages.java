// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains the mapping of packages identifiers to packages and is able to
 * initialize new ones if needed. Packages contain sets of classes
 *
 * There is a special package which is used by specially generated classes such
 * as those for primitive types and arrays. This special package cannot be used
 * by any class within the virtual machine.
 *
 * @since 2017/06/15
 */
public class Packages
{
	/** The reference state which these packages are a part of. */
	protected final Reference<LinkerState> linkerstate;
	
	/**
	 * Initializes the package manager.
	 *
	 * @param __ls The owning linker state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/15
	 */
	Packages(Reference<LinkerState> __ls)
		throws NullPointerException
	{
		// Check
		if (__ls == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.linkerstate = __ls;
		
		throw new todo.TODO();
	}
}

