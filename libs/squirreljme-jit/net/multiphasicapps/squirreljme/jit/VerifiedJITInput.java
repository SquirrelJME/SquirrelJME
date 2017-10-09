// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.rc.NoSuchResourceException;
import net.multiphasicapps.squirreljme.jit.rc.Resource;
import net.multiphasicapps.squirreljme.jit.verifier.FamilyTree;
import net.multiphasicapps.squirreljme.jit.verifier.VerifiedClass;
import net.multiphasicapps.squirreljme.jit.verifier.VerifiedClassTree;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This class contains input for the JIT which has completely been verified
 * to be correct and well forming. After the verification step, the compiler
 * needs to perform far less verification actions to determine if things are
 * correct.
 *
 * This is an optimization which performs all verification in a single step
 * which means that it can fail fast. It also means code generation is faster
 * and there does not need to guessing as to what is valid and what is not.
 * Essentially, during the compilation phase things can be taken for
 * granted.
 *
 * @since 2017/10/03
 */
public final class VerifiedJITInput
{
	/** The tree of classes. */
	protected final VerifiedClassTree tree;
	
	/** Groups which are available. */
	private final Map<String, JITInputGroup> _groups;
	
	/**
	 * Initializes the verified input.
	 *
	 * @param __tree The tree of classes.
	 * @param __g Groups, this is used directly. 
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/03
	 */
	private VerifiedJITInput(VerifiedClassTree __tree,
		Map<String, JITInputGroup> __g)
		throws NullPointerException
	{
		if (__tree == null)
			throw new NullPointerException("NARG");
		
		this.tree = __tree;
		this._groups = __g;
	}
	
	/**
	 * Obtains the specified class from the family tree.
	 *
	 * @param __n The name of the class to get the node for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/08
	 */
	public VerifiedClass getNode(ClassName __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		return this.tree.get(__n);
	}
	
	/**
	 * Loads a resource from the given group.
	 *
	 * @param __g The group to look within.
	 * @param __f The file to load.
	 * @return The input stream to the resource.
	 * @throws NoSuchGroupException If the group does not exist.
	 * @throws NoSuchResourceException If the resource does not exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/05
	 */
	public InputStream loadResource(String __g, String __f)
		throws NoSuchResourceException, NullPointerException
	{
		if (__g == null || __f == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI32 No such group with the given name exists.
		// (The name of the group)}
		Map<String, JITInputGroup> groups = this._groups;
		JITInputGroup grp = groups.get(__g);
		if (grp == null)
			throw new NoSuchGroupException(String.format("JI32 %s", __g));
		
		// Load from group
		return grp.loadResource(__f);
	}
	
	/**
	 * Verifies the input and returns a verified input
	 *
	 * @param __i The input for the JIT.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/03
	 */
	public static VerifiedJITInput verify(JITInput __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

