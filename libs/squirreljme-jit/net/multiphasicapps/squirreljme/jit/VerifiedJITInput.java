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
import net.multiphasicapps.squirreljme.jit.verifier.ClassStructures;
import net.multiphasicapps.squirreljme.jit.verifier.FamilyTree;
import net.multiphasicapps.squirreljme.jit.verifier.VerifiedMethods;
import net.multiphasicapps.collections.SortedTreeMap;
import net.multiphasicapps.collections.UnmodifiableMap;
import net.multiphasicapps.collections.UnmodifiableSet;

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
	/** The input for the JIT. */
	protected final JITInput input;
	
	/** The tree of classes that are available. */
	protected final FamilyTree tree;
	
	/** The structures of fields and methods within classes. */
	protected final ClassStructures structures;
	
	/** Methods which have been verified. */
	protected final VerifiedMethods methods;
	
	/**
	 * Initializes the verified input.
	 *
	 * @param __tree The tree of classes.
	 * @param __input The input classes and groups.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/03
	 */
	private VerifiedJITInput(JITInput __input, FamilyTree __tree,
		ClassStructures __structs, VerifiedMethods __methods)
		throws NullPointerException
	{
		if (__tree == null || __input == null || __structs == null ||
			__methods == null)
			throw new NullPointerException("NARG");
		
		this.tree = __tree;
		this.input = __input;
		this.structures = __structs;
		this.methods = __methods;
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
		
		// {@squirreljme.error JI0d No such group with the given name exists.
		// (The name of the group)}
		JITInputGroup grp = this.input.getGroup(__g);
		if (grp == null)
			throw new NoSuchGroupException(String.format("JI0d %s", __g));
		
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
		
		// Setup family tree
		FamilyTree tree = new FamilyTree(__i);
		
		// Determine the structure classes are within
		ClassStructures structs = new ClassStructures(tree);
		
		// Setup all methods
		VerifiedMethods methods = new VerifiedMethods(__i, structs);
		
		// Setup verified input
		return new VerifiedJITInput(__i, tree, structs, methods);
	}
}

