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
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassFlags;

/**
 * This is used to build nodes within the tree.
 *
 * @since 2017/10/09
 */
final class __NodeBuilder__
{
	/** The class file containing most of the class data. */
	protected final ClassFile file;
	
	/** Reference to the owning tree. */
	private final Reference<__TreeBuilder__> _treeref;
	
	/** Has this finished the inheritence stage? */
	private volatile boolean _didinherits;
	
	/**
	 * Initializes the node builder for the tree data.
	 *
	 * @param __tr The reference to the owning tree.
	 * @param __m The map of nodes to be pre-placed into.
	 * @param __f The class to verify.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the class could not be verified.
	 * @since 2017/10/09
	 */
	__NodeBuilder__(Reference<__TreeBuilder__> __tr,
		Map<ClassName, __NodeBuilder__> __m, ClassFile __f)
		throws NullPointerException, VerificationException
	{
		if (__tr == null || __m == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Used to refer to the tree while allowing it to be garbage
		// collected
		this._treeref = __tr;
		
		// Flags are needed before these are fully constructed
		this.file = __f;
		
		// Add self to the map
		ClassName thisname = __f.thisName();
		__m.put(thisname, this);
		
		// Debug
		System.err.printf("DEBUG -- Verifying node %s%n", thisname);
		
		// Will need to work with the owning tree
		__TreeBuilder__ tree = __treeBuilder();
		
		// Check to make sure the super class is valid and has no
		// circular inheritence to it
		ClassName supername = __f.superName();
		__NodeBuilder__ supernode;
		if (supername != null)
		{
			// {@squirreljme.error JI39 Circular inheritence detected in
			// super class tree. (This class; The super class)}
			supernode = tree.get(supername);
			if (!supernode._didinherits)
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
			throw new todo.TODO();
		}
		
		// No super node, set this because it is used later to determine the
		// method lookup
		else
			supernode = null;
		
		if (true)
			throw new todo.TODO();
		
		// The initial inheritence stage has been completed for this node
		// This flag should never normally be read by the inheritence check
		// because if it does, it means that there is a circular reference
		// for classes
		this._didinherits = true;
		
		throw new todo.TODO();
		
	}
	
	/**
	 * Returns the flags for this class.
	 *
	 * @return The class flags.
	 * @since 2017/10/09
	 */
	public final ClassFlags flags()
	{
		return this.file.flags();
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2017/10/09
	 */
	public final ClassName thisName()
	{
		return this.file.thisName();
	}
	
	/**
	 * Returns the owning tree builder.
	 *
	 * @return The tree which is building this node.
	 * @throws IllegalStateException If the tree has been garbage collected.
	 * @since 2017/10/09
	 */
	final __TreeBuilder__ __treeBuilder()
		throws IllegalStateException
	{
		// {@squirreljme.error JI36 The class tree builder has been garbage
		// collected.}
		__TreeBuilder__ rv = this._treeref.get();
		if (rv == null)
			throw new IllegalStateException("JI36");
		return rv;
	}
	
	/**
	 * Is the specified class visible from a specified class?
	 *
	 * @param __from The source class.
	 * @param __cansee The class to see if {@code __from} is visible to it.
	 * @return The visibility.
	 */
	static final boolean __isClassVisibleFrom(__NodeBuilder__ __from,
		__NodeBuilder__ __cansee)
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

