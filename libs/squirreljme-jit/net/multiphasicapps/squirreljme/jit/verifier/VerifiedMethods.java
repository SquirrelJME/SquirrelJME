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
import net.multiphasicapps.squirreljme.jit.cff.ByteCode;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.Method;
import net.multiphasicapps.squirreljme.jit.JITInput;

/**
 * This class is used to contain all of the methods which exist within the
 * class structures and is used to verify that all of them are valid.
 *
 * @since 2017/10/09
 */
public final class VerifiedMethods
{
	/**
	 * Initializes the verification of methods, initializing every method
	 * which exists within the input structures.
	 *
	 * @param __i The input classes to be verified.
	 * @param __structs The structures which make up classes.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If verification fails.
	 * @since 2017/10/09
	 */
	public VerifiedMethods(JITInput __i, ClassStructures __structs)
		throws NullPointerException, VerificationException
	{
		if (__i == null || __structs == null)
			throw new NullPointerException("NARG");
		
		// Go through all classes and all methods to verify them
		for (ClassFile f : __i.classFiles())
		{
			// Debug
			System.err.printf("DEBUG -- Verify methods in %s%n", f.thisName());
			
			// Get the structure for this class
			ClassName cname = f.thisName();
			ClassStructure struct = __structs.get(cname);
			
			// Go through all methods
			for (Method m : f.methods())
			{
				// Only verify methods which actually have code
				ByteCode code = m.byteCode();
				if (code == null)
					continue;
				
				// Look into the structure to determine the method index
				if (true)
					throw new todo.TODO();
				
				// Create verified method for it
				throw new todo.TODO();
			}
			
			throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}

