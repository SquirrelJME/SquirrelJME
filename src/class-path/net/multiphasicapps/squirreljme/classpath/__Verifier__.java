// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIException;

/**
 * This verifies that the specified class is properly laid out in the structure
 * required by the virtual machine.
 *
 * @since 2016/05/28
 */
class __Verifier__
{
	/** The source for classes. */
	protected final ClassPath classpath;
	
	/** The class to verify. */
	protected final CIClass verify;
	
	/** The name of this class. */
	protected final ClassNameSymbol thisname;
	
	/**
	 * This performs the 
	 *
	 * @param __cp The class path to use when finding classes to verify.
	 * @param __cl The class to be verified.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/28
	 */
	__Verifier__(ClassPath __cp, CIClass __cl)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classpath = __cp;
		this.verify = __cl;
		this.thisname = __cl.thisName();
		
		// Basic checks
		__basic();
		
		// Check class circulary
		List<CIClass> supers = new LinkedList<>();
		List<CIClass> inters = new LinkedList<>();
		__circularity(supers, inters);
	}
	
	/**
	 * Performs basic verification checks.
	 *
	 * @since 2016/05/28
	 */
	private void __basic()
	{
		// Cache
		ClassPath classpath = this.classpath;
		CIClass verify = this.verify;
	}
	
	/**
	 * Checks if the class circularly depends on itself
	 *
	 * @param __supers The output super classes.
	 * @param __inters the output interfaces.
	 * @since 2016/05/28
	 */
	private void __circularity(List<CIClass> __supers,
		List<CIClass> __inters)
	{
		// Cache
		ClassPath classpath = this.classpath;
		CIClass verify = this.verify;
		
		// Make sure all interfaces are actually interfaces
		Set<ClassNameSymbol> ints = new HashSet<>();
		for (ClassNameSymbol in : verify.interfaceNames())
		{
			// {@squirreljme.error BN02 Could not find the class which the
			// current class implements. (This class; The interface)}
			CIClass inc = classpath.locateClass(in);
			if (inc == null)
				throw new CIException(String.format("BN02 %s %s",
					this.thisname, in));
			
			// {@squirreljme.error BN03 (This class; The interface)}
			if (!inc.flags().isInterface())
				throw new CIException(String.format("BN03 %s %s",
					this.thisname, in));
			
			// Add to interface list
			__inters.add(inc);
		}
		
		throw new Error("TODO");
	}
}

