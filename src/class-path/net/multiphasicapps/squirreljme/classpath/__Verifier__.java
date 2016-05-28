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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIAccessibleFlags;
import net.multiphasicapps.squirreljme.ci.CIAccessibleObject;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIClassFlags;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.ci.CIMethodID;

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
		
		// Check method implementations
		__methods(supers, inters);
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
	 * Checks if the class circularly depends on itself.
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
		ClassNameSymbol thisname = this.thisname;
		
		// Make sure all interfaces are actually interfaces
		Set<ClassNameSymbol> ints = new HashSet<>();
		for (ClassNameSymbol in : verify.interfaceNames())
		{
			// {@squirreljme.error BN08 Class implements itself. (This class;
			// The interface)}
			if (in.equals(thisname))
				throw new CIException(String.format("BN08 %s %s",
					thisname, in));
			
			// {@squirreljme.error BN02 Could not find the class which the
			// current class implements. (This class; The interface)}
			CIClass inc = classpath.locateClass(in);
			if (inc == null)
				throw new CIException(String.format("BN02 %s %s",
					thisname, in));
			
			// {@squirreljme.error BN03 Class does not implement an interface.
			// (This class; The interface)}
			if (!inc.flags().isInterface())
				throw new CIException(String.format("BN03 %s %s",
					thisname, in));
			
			// {@squirreljme.error BN04 Class already implements the given
			// interface. (This class; The interface)}
			if (__inters.contains(inc))
				throw new CIException(String.format("BN04 %s %s",
					thisname, in));
			
			// Add to interface list
			__inters.add(inc);
		}
		
		// Get the super class
		ClassNameSymbol dsn = verify.superName();
		
		// {@squirreljme.error BN09 java.lang.Object cannot extend another
		// class and must be the root of the class tree. (This class; The super
		// class it should not extend)}
		boolean isobj = thisname.equals("java/lang/Object");
		if (isobj && dsn != null)
			throw new CIException(String.format("BN09 %s %s", thisname,
				dsn));
		
		// {@squirreljme.error BN0a Non-java.lang.Object class does not extend
		// a class. (This class)}
		else if (!isobj && dsn == null)
			throw new CIException(String.format("BN0a %s", thisname));
		
		// {@squirreljme.error BN0b Interfaces must extend java.lang.Object.
		// (This class; The super class)}
		else if (!isobj && verify.flags().isInterface() &&
			!dsn.equals("java/lang/Object"))
			throw new CIException(String.format("BN0b %s %s", thisname, dsn));
		
		// Make sure classes do not eventually extend self
		for (ClassNameSymbol sn = dsn; sn != null;)
		{
			// {@squirreljme.error BN07 Class eventually extends itself.
			// (This class; The super class)}
			if (sn.equals(thisname))
				throw new CIException(String.format("BN07 %s %s", thisname,
					sn));
			
			// {@squirreljme.error BN05 Could not locate the super class of
			// the current class. (This class; The super class)}
			CIClass scl = classpath.locateClass(sn);
			if (scl == null)
				throw new CIException(String.format("BN05 %s %s",
					thisname, sn));
			
			// Add to list of super-classes
			__supers.add(scl);
			
			// {@squirreljme.error BN06 Class extends an interface or a final
			// class. (This class; The super class; The super class flags)}
			CIClassFlags scf = scl.flags();
			if (scf.isInterface() || scf.isFinal())
				throw new CIException(String.format("BN06 %s %s %s",
					thisname, sn, scf));
			
			// Get next super class
			sn = scl.superName();
		}
	}
	
	/**
	 * Checks that non-private final methods are not overridden, that
	 * abstract methods are implemented (if the class is not abstract), and
	 * that methods in higher classes do not 
	 *
	 * @param __supers The output super classes.
	 * @param __inters the output interfaces.
	 * @since 2016/05/28
	 */
	private void __methods(List<CIClass> __supers,
		List<CIClass> __inters)
	{
		// Cache
		ClassPath classpath = this.classpath;
		CIClass verify = this.verify;
		CIClassFlags vflags = verify.flags();
		
		// Is this class abstract?
		boolean visabs = vflags.isAbstract();
		
		// Start with methods that are at the top-most level to check against
		// all the methods that may be replaced
		Map<CIMethodID, CIMethod> toplevel =
			new HashMap<>();
		for (Map.Entry<CIMethodID, CIMethod> e : verify.methods().entrySet())
		{
			// Get key, value, and flags
			CIMethodID k = e.getKey();
			CIMethod v = e.getValue();
			CIMethodFlags f = v.flags();
			
			// Ignore static and constructors
			if (f.isStatic() || k.isConstructor())
				continue;
			
			// {@squirreljme.error BN0c Non-abstract class contains an abstract
			// method. (This class; The method identifier; The method flags)}
			if (!visabs && f.isAbstract())
				throw new CIException(String.format("BN0c %s %s %s",
					this.thisname, k, f));
			
			// Add it
			toplevel.put(k, v);
		}
		
		// Go through all classes
		int ns = __supers.size(), ni = __inters.size(), n = ns + ni;
		for (int i = 0; i < n; i++)
		{
			// Get the super class
			CIClass scl = (i < ns ? __supers.get(i) : __inters.get(i - ns));
			
			// Go through the methods in this class
			for (Map.Entry<CIMethodID, CIMethod> e : scl.methods().entrySet())
			{
				// Get key, value, and flags
				CIMethodID k = e.getKey();
				CIMethod v = e.getValue();
				CIMethodFlags f = v.flags();
				
				// Also get top-level details
				CIMethod tlv = toplevel.get(k);
			
				// Ignore static and constructors
				if (f.isStatic() || k.isConstructor())
					continue;
				
				// If top-level class does not have the given method then add
				// the implementation of it
				if (tlv == null)
					toplevel.put(k, (tlv = v));
				
				// Get flags for top level
				CIMethodFlags tlf = tlv.flags();
				
				// Compare flags for the methods
				int fcomp = __Verifier__.<CIMethodFlags>__compare(f, tlf);
				
				// {@squirreljme.error BN0d Top-level method has more strict
				// access compared to super-method. (Super method flags;
				// Top-level method flags)}
				if (fcomp > 0)
					throw new CIException(String.format("BN0d %s %s", f, tlf));
				
				throw new Error("TODO");
			}
		}
	}
	
	/**
	 * Compares access flags in the order of private, package private,
	 * protected, then public.
	 *
	 * @param <F> The accessible flag type.
	 * @param __sao Flags used in a super-class version of this.
	 * @param __tao Flags used in the current class version of this.
	 * @return The 
	 * @throws
	 */
	private static <F extends CIAccessibleFlags> int __compare(F __sao,
		F __tao)
		throws NullPointerException
	{
		// Check
		if (__sao == null || __tao == null)
			throw new NullPointerException("NARG");
		
		// Get access level of each
		int sl = (__sao.isPublic() ? 4 : (__sao.isProtected() ? 3 :
				(__sao.isPackagePrivate() ? 2 : 1))),
			tl = (__tao.isPublic() ? 4 : (__tao.isProtected() ? 3 :
				(__tao.isPackagePrivate() ? 2 : 1)));
		
		// Compare these
		if (sl < tl)
			return -1;
		else if (sl > tl)
			return 1;
		return 0;
	}
}

