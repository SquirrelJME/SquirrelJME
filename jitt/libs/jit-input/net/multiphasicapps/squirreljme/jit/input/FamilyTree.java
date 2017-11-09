// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.input;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.classfile.ClassFile;
import net.multiphasicapps.squirreljme.jit.classfile.ClassName;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This contains the family tree of classes and is used to store the
 * inheritence information of classes. It only contains information on which
 * classes extend other classes.
 *
 * This class will handle and verify input of nodes within the tree as they
 * are demanded. As such with lazy initialization, information is slowly
 * built up.
 *
 * This class is thread safe.
 *
 * @since 2017/10/09
 */
public final class FamilyTree
{
	/** Lock for tree access. */
	final Object _lock =
		new Object();
	
	/** The input for the JIT. */
	protected final JITInput input;
	
	/** Nodes that exist within the family tree. */
	private final Map<ClassName, FamilyNode> _nodes =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the family tree from the given base input classes.
	 *
	 * @param __i The class input to use for when the tree is generated.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the tree could not be verified.
	 * @since 2017/10/09
	 */
	public FamilyTree(JITInput __i)
		throws NullPointerException, VerificationException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.input = __i;
	}
	
	/**
	 * Obtains the specified node from the family tree.
	 *
	 * @param __n The node to get.
	 * @return The node for the given class.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the family could not be verified.
	 * @since 2017/10/10
	 */
	public final FamilyNode get(ClassName __n)
		throws NullPointerException, VerificationException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Nodes are dynamically generated as needed
		Map<ClassName, FamilyNode> nodes = this._nodes;
		synchronized (this._lock)
		{
			// If it exists, use it
			FamilyNode rv = nodes.get(__n);
			if (rv != null)
				return rv;
			
			// Which classfile should be used?
			ClassFile file;
			if (__n.isArray() || __n.isPrimitive())
				file = ClassFile.special(__n.field());
			else
				file = this.input.getClass(__n);
			
			// Setup node
			return new FamilyNode(new WeakReference<>(this), file, nodes);
		}
	}
}

