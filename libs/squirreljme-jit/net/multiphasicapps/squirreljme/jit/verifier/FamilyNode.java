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
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;

/**
 * This represents a single node within the family tree.
 *
 * @since 2017/10/05
 */
public final class FamilyNode
{
	/** The reference to the outer tree. */
	protected final Reference<FamilyTree> tree;
	
	/** The class file this represents. */
	protected final ClassFile classfile;
	
	/**
	 * Initializes the node in the family tree.
	 *
	 * @param __tr The tree which contains this node.
	 * @param __f The class file to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/05
	 */
	FamilyNode(Reference<FamilyTree> __tr, ClassFile __f)
		throws NullPointerException
	{
		if (__tr == null || __f == null)
			throw new NullPointerException("NARG");
		
		this.tree = __tr;
		this.classfile = __f;
	}
}

