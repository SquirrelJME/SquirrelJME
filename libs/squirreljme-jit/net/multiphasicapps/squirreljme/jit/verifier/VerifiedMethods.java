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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.cff.ByteCode;
import net.multiphasicapps.squirreljme.jit.cff.ClassFile;
import net.multiphasicapps.squirreljme.jit.cff.ClassName;
import net.multiphasicapps.squirreljme.jit.cff.Method;
import net.multiphasicapps.squirreljme.jit.cff.MethodHandle;
import net.multiphasicapps.squirreljme.jit.JITInput;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This class is used to contain all of the methods which exist within the
 * class structures and is used to verify that all of them are valid.
 *
 * @since 2017/10/09
 */
public final class VerifiedMethods
{
	/** The order in which methods exist. */
	private final List<VerifiedMethod> _order =
		new ArrayList<>();
	
	/** The map of verified methods. */
	private final Map<MethodHandle, VerifiedMethod> _methods =
		new SortedTreeMap<>();
	
	/** The list of methods in their verified order. */
	private volatile Reference<List<VerifiedMethod>> _roorder;
	
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
		
		// Used to index methods in a fixed order
		List<VerifiedMethod> order = this._order;
		Map<MethodHandle, VerifiedMethod> methods = this._methods;
		int nextmethod = 0;
		
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
				
				// Methods only need to be verified once
				MethodHandle mdx = m.handle();
				VerifiedMethod vm = methods.get(mdx);
				if (vm != null)
					continue;
				
				// Verify it
				vm = VerifiedMethod.__verify(__structs, mdx, nextmethod++,
					code);
				methods.put(mdx, vm);
				order.add(vm);
			}
		}
	}
	
	/**
	 * Returns the methods which appear in their verification order.
	 *
	 * @return The list of methods in their verification order.
	 * @since 2017/10/14
	 */
	public final List<VerifiedMethod> order()
	{
		Reference<List<VerifiedMethod>> ref = this._roorder;
		List<VerifiedMethod> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._roorder = new WeakReference<>(
				(rv = UnmodifiableList.<VerifiedMethod>of(this._order)));
		
		return rv;
	}
}

