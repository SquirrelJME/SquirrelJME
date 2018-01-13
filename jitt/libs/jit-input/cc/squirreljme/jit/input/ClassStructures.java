// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.input;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import cc.squirreljme.jit.classfile.ClassName;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This contains the structure of every class which is available to the
 * virtual machine.
 *
 * Like {@link FamilyTree} this class is lazily initialized.
 *
 * This class is thread safe.
 *
 * @since 2017/10/09
 */
public final class ClassStructures
{
	/** Lock for safety. */
	final Object _lock =
		new Object();
	
	/** The source class tree with inheritence information. */
	protected final FamilyTree tree;
	
	/** Structures that exist within the run-time. */
	private final Map<ClassName, ClassStructure> _structs =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the class structures.
	 *
	 * @param __tree The family tree of classes.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the class structure could not be
	 * verified.
	 * @since 2017/10/09
	 */
	public ClassStructures(FamilyTree __tree)
		throws NullPointerException, VerificationException
	{
		if (__tree == null)
			throw new NullPointerException("NARG");
		
		this.tree = __tree;
	}
	
	/**
	 * Obtains the structure for the given class.
	 *
	 * @param __n The structure of the class to get.
	 * @return The structure of the given class.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the structure could not be verified.
	 * @since 2017/10/10
	 */
	public final ClassStructure get(ClassName __n)
		throws NullPointerException, VerificationException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// The structures are dynamically generated
		Map<ClassName, ClassStructure> structs = this._structs;
		synchronized (this._lock)
		{
			// Use pre-existing class
			ClassStructure rv = structs.get(__n);
			if (rv != null)
				return rv;
			
			// Generate it
			structs.put(__n, (rv = new ClassStructure(
				new WeakReference<>(this), tree, __n)));
			return rv;
		}
	}
}

