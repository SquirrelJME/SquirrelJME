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
import java.util.Objects;
import net.multiphasicapps.squirreljme.jit.cff.BinaryName;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassFlags;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;

/**
 * This represents a single node within the class inheritence tree.
 *
 * @since 2017/10/09
 */
public final class FamilyNode
{
	/** The class file to refer to. */
	protected final ClassFile classfile;
	
	/** Reference to the owning tree. */
	private final Reference<FamilyTree> _treeref;
	
	/** This is used to detect cyclic inheritence? */
	private volatile boolean _finished;
	
	/**
	 * Initializes the node within the tree.
	 *
	 * @param __tr The owning tree reference.
	 * @param __f The class file representation for this class.
	 * @param __ns The node map where this node is placed.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If there is a circular dependency within
	 * the loaded classes.
	 * @since 2017/10/09
	 */
	FamilyNode(Reference<FamilyTree> __tr, ClassFile __f,
		Map<ClassName, FamilyNode> __ns)
		throws NullPointerException, VerificationException
	{
		if (__tr == null || __f == null || __ns == null)
			throw new NullPointerException("NARG");
		
		this._treeref = __tr;
		this.classfile = __f;
		
		// Add self node to the node tree, this is needed because nodes will
		// refer to other nodes which are not initialized yet. Also it needs
		// to be detected whether uninitialized nodes are referred to.
		ClassName thisname = __f.thisName();
		__ns.put(thisname, this);
		
		// Source from this tree
		FamilyTree tree = __tr.get();
		
		// Check to make sure the super class is valid and has no
		// circular inheritence to it
		ClassName supername = __f.superName();
		FamilyNode supernode;
		if (supername != null)
		{
			// {@squirreljme.error JI39 Circular inheritence detected in
			// super class tree. (This class; The super class)}
			supernode = tree.get(supername);
			if (!supernode._finished)
				throw new VerificationException(String.format("JI39 %s %s",
					thisname, supername));
			
			// {@squirreljme.error JI37 The specified class cannot extend the
			// other class because it has the incorrect flags. (The name of
			// this class; The name of the super class; The super class flags)}
			ClassFlags superflags = supernode.flags();
			if (superflags.isFinal() || superflags.isInterface())
				throw new VerificationException(String.format("JI37 %s %s %s",
					thisname, supername, superflags));
			
			// {@squirreljme.error JI38 The current class cannot extend the
			// specified class because it is not visible. (The name of this
			// class; The name of the super class; The super class flags)}
			if (!__isClassVisibleFrom(this, supernode))
				throw new VerificationException(String.format("JI38 %s %s %s",
					thisname, supername, superflags));
		}
		
		// No super node, set this because it is used later to determine the
		// method lookup
		else
			supernode = null;
		
		// Handle all interfaces
		for (ClassName interfacename : __f.interfaceNames())
		{
			// {@squirreljme.error JI3a Circular inheritence detected in
			// interface class tree. (This class; The interface class)}
			FamilyNode interfacenode = tree.get(interfacename);
			if (!interfacenode._finished)
				throw new VerificationException(String.format("JI3a %s %s",
					thisname, interfacename));
			
			// {@squirreljme.error JI3b The specified class cannot implement
			// the other class because it has the incorrect flags. (The name of
			// this class; The interface class; The interface class flags)}
			ClassFlags interfaceflags = interfacenode.flags();
			if (!interfaceflags.isInterface())
				throw new VerificationException(String.format("JI3b %s %s %s",
					thisname, interfacename, interfaceflags));
			
			// {@squirreljme.error JI3c The current class cannot implement the
			// specified class because it is not visible. (The name of this
			// class; The interface class; The interface class flags)}
			if (!__isClassVisibleFrom(this, interfacenode))
				throw new VerificationException(String.format("JI3c %s %s %s",
					thisname, interfacename, interfaceflags));
		}
		
		// Cannot generate cyclic inheritence anymore
		this._finished = true;
	}
	
	/**
	 * Returns the flags for this class.
	 *
	 * @return The class flags.
	 * @since 2017/10/09
	 */
	public final ClassFlags flags()
	{
		return this.classfile.flags();
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2017/10/09
	 */
	public final ClassName thisName()
	{
		return this.classfile.thisName();
	}
	
	/**
	 * Is the specified class visible from a specified class?
	 *
	 * @param __from The source class.
	 * @param __cansee The class to see if {@code __from} is visible to it.
	 * @return The visibility.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	static final boolean __isClassVisibleFrom(FamilyNode __from,
		FamilyNode __cansee)
		throws NullPointerException
	{
		if (__from == null || __cansee == null)
			throw new NullPointerException("NARG");
		
		// Get the package of both classes
		BinaryName pa = __from.thisName().inPackage(),
			pb = __from.thisName().inPackage();
		
		// If in the same package, always visible
		if (Objects.equals(pa, pb))
			return true;
		
		// Otherwise only if the target class is public
		ClassFlags tf = __cansee.flags();
		return tf.isPublic();
	}
}

