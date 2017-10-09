// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.verifier;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;

/**
 * This is used to build nodes within the tree.
 *
 * @since 2017/10/09
 */
class __NodeBuilder__
{
	/**
	 * Initializes the node builder for the tree data.
	 *
	 * @param __tr The reference to the owning tree.
	 * @param __m The map of nodes to be pre-placed into.
	 * @param __f The class to verify.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	__NodeBuilder__(Reference<__TreeBuilder__> __tr,
		Map<ClassName, __NodeBuilder__> __m, ClassFile __f)
		throws NullPointerException
	{
		if (__tr == null || __m == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Add self to the map
		ClassName thisname = __f.thisName();
		__m.put(thisname, this);
		
		// Debug
		System.err.printf("DEBUG -- Verifying node %s%n", thisname);
		
		throw new todo.TODO();
	}
}

