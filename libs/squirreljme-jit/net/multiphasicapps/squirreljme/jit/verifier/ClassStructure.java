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
import net.multiphasicapps.squirreljme.jit.cff.ClassName;

/**
 * This contains the structure information for a single class. The class
 * contains a structure of instance and static members. This information is
 * used to determine how large a class is and which methods should be
 * invoked when required.
 *
 * @since 2017/10/09
 */
public final class ClassStructure
{
	/**
	 * Initializes the individual class structure.
	 *
	 * @param __csr The owning class reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/10
	 */
	ClassStructure(Reference<ClassStructures> __csr, FamilyTree __tree,
		ClassName __cn)
		throws NullPointerException
	{
		if (__csr == null || __tree == null || __cn == null)
			throw new NullPointerException("NARG");
		
		// Need to node to determine which methods are replacable or not
		ClassStructures structs = __csr.get();
		FamilyNode node = __tree.get(__cn);
		
		// Inherit all methods from super classes.
		// Static, private, and package private (in another package)
		ClassName supername = node.superName();
		if (supername != null)
		{
			ClassStructure superstruct = structs.get(supername);
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}

