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
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is used to construct a family tree which would then be completely
 * verified as the end result.
 *
 * @since 2017/10/09
 */
class __TreeBuilder__
{
	/** The mapping of available files. */
	final Map<ClassName, ClassFile> _files =
		new SortedTreeMap<>();
	
	/** Nodes which are to exist within the tree. */
	private final Map<ClassName, __NodeBuilder__> _nodes =
		new SortedTreeMap<>();
	
	/** The queue of classes to process. */
	private final Deque<ClassName> _queue =
		new LinkedList<>();
	
	/**
	 * Initializes the tree builder.
	 *
	 * @param __f The input classes to process.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	__TreeBuilder__(Collection<ClassFile> __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Go through all classes 
		Map<ClassName, ClassFile> files = this._files;
		Deque<ClassName> queue = this._queue;
		for (ClassFile f : __f)
		{
			ClassName n = f.thisName();
			
			// Used for referencing nodes
			files.put(n, f);
			
			// Things which need to be processed
			queue.offerLast(n);
		}
	}
	
	/**
	 * Builds the class tree and all of its nodes.
	 *
	 * @return The verified class tree.
	 * @throws VerificationException If the classes could not be verified.
	 * @since 2017/10/09
	 */
	public VerifiedClassTree build()
		throws VerificationException
	{
		// Just constantly try to obtain nodes
		Deque<ClassName> queue = this._queue;
		while (!queue.isEmpty())
			get(queue.removeFirst());
		
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the node with the given name.
	 *
	 * @param __n The node to get.
	 * @return The resulting node.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If it could not be verified.
	 * @since 2017/10/09
	 */
	public __NodeBuilder__ get(ClassName __n)
		throws NullPointerException, VerificationException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Use existing node if it exists
		Map<ClassName, __NodeBuilder__> nodes = this._nodes;
		__NodeBuilder__ rv = nodes.get(__n);
		if (rv != null)
			return rv;
		
		// Obtain the class file
		Map<ClassName, ClassFile> files = this._files;
		ClassFile file = files.get(__n);
		
		// Need to dynamically create a class representation?
		// Note that dynamically loaded classes here do not need to actually
		// be placed into the queue because they are initialized here
		if (file == null)
		{
			// {@squirreljme.error JI34 Cannot create the node for the
			// specified class because it does not exist, only arrays and
			// primitive types are dynamically generated. (The name of the
			// class)}
			if (!__n.isArray() && !__n.isPrimitive())
				throw new VerificationException(String.format("JI34 %s", __n));
			
			throw new todo.TODO();
		}
		
		// Setup the node
		return new __NodeBuilder__(new WeakReference<>(this), nodes, file);
	}
}

