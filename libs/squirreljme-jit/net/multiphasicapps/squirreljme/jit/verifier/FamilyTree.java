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
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the family tree which contains a large number of nodes for each
 * class which exists in the virtual machine.
 *
 * @since 2017/10/05
 */
public final class FamilyTree
{
	/** The family tree. */
	private final Map<ClassName, FamilyNode> nodes =
		new SortedTreeMap<>();
	
	/**
	 * This constructs the family tree.
	 *
	 * @param __n The classes which make up the tree.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the tree structure cannot be built.
	 * @since 2017/10/05
	 */
	public FamilyTree(Collection<ClassFile> __n)
		throws NullPointerException, VerificationException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Reference to this tree
		Reference<FamilyTree> self = new WeakReference<>(this);
		
		// Build nodes, recursion checks are done lazily
		Map<ClassName, FamilyNode> nodes = this.nodes;
		for (ClassFile f : __n)
			nodes.put(f.thisName(), new FamilyNode(self, f));
	}
}

