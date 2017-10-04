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

import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is used and performs the verification process of all of the
 * input classes to determine if they are valid in the virtual machine.
 *
 * @since 2017/10/04
 */
public final class Verifier
{
	/** The classes to verify. */
	protected final Map<ClassName, ClassFile> inclasses;
	
	/**
	 * This initializes the verifier for the given input classes.
	 *
	 * @param __icl The input classes to verify for.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/04
	 */
	public Verifier(Map<ClassName, ClassFile> __icl)
		throws NullPointerException
	{
		if (__icl == null)
			throw new NullPointerException("NARG");
		
		this.inclasses = __icl;
	}
	
	/**
	 * Runs the verifier.
	 *
	 * @throws VerificationException If verification of the classes fails.
	 * @since 2017/10/04
	 */
	public void run()
		throws VerificationException
	{
		Map<ClassName, ClassFile> inclasses = this.inclasses;
		
		// First setup working nodes which contains lists of what things are
		// and what they implement
		Map<ClassName, __WorkingClassNode__> nodes = new SortedTreeMap<>();
		for (ClassFile f : inclasses.values())
		{
			ClassName n = f.thisName();
			
			throw new todo.TODO();
		}
		
		// The first thing to do is to build the hierarchy and web of the
		// classes, such as which classes implement what and their superclasses
		// and superinterfaces. Basically, there are super classes and
		// implemented interfaces. Instanceof checks are effectively either
		// super classes or implemented interfaces
		throw new todo.TODO();
	}
}

