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
import net.multiphasicapps.squirreljme.jit.NoSuchClassException;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the family tree which contains a large number of nodes for each
 * class which exists in the virtual machine.
 *
 * @since 2017/10/05
 */
public final class FamilyTree
{
	/** Lock for thread-safety, for when arrays need to be created. */
	final Object _lock =
		new Object();
	
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
	
	/**
	 * Returns the node for the given class.
	 *
	 * @param __n The node for the given class.
	 * @throws NoSuchClassException If the specifie class does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	public final FamilyNode get(ClassName __n)
		throws NoSuchClassException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Lock because there could be multiple threads trying to access
		// classes and it is very possible for array types to be automatically
		// generated as they are needed
		// The same goes for primitive types
		Map<ClassName, FamilyNode> nodes = this.nodes;
		synchronized (this._lock)
		{
			// If it exists in the map then use that
			FamilyNode rv = nodes.get(__n);
			if (rv != null)
				return rv;
			
			// {@squirreljme.error JI34 Cannot create the node for the
			// specified class because it does not exist, only arrays and
			// primitive types are dynamically generated. (The name of the
			// class)}
			if (!__n.isArray() && !__n.isPrimitive())
				throw new NoSuchClassException(String.format("JI34 %s", __n));
			
			throw new todo.TODO();
		}
	}
}

