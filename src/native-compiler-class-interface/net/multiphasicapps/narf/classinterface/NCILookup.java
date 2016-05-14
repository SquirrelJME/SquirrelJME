// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This is the base class which provides access to classes as an entire
 * library. This is used by the native compile to look at the details of a
 * class to determine how the code should be compiled.
 *
 * @since 2016/04/20
 */
public abstract class NCILookup
{
	/** The loaded class library. */
	private final Map<ClassNameSymbol, Reference<NCIClass>> _loaded =
		new HashMap<>();
	
	/**
	 * Initializes the class library.
	 *
	 * @since 2016/04/20
	 */
	public NCILookup()
	{
	}
	
	/**
	 * Loads the class with the given name.
	 *
	 * @param __bn The binary name of class.
	 * @return The class with the given or {@code null} if it does not exist.
	 * @throws NCIException If the class exists however it is not
	 * formed correctly or could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	protected abstract NCIClass internalClassLookup(BinaryNameSymbol __bn)
		throws NCIException, NullPointerException;
	
	/**
	 * Checks whether the current byte code (the method that contains this byte
	 * code) can access the specified accessible object.
	 *
	 * @param __from The object to act as the check source.
	 * @param __ao The object to check access against.
	 * @return {@code true} if the object can be accessed.
	 * @throws NCIException If the class has no set access flag type or is
	 * a class which eventually extends itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public final boolean canAccess(NCIAccessibleObject __from,
		NCIAccessibleObject __ao)
		throws NCIException, NullPointerException
	{
		// Check
		if (__from == null || __ao == null)
			throw new NullPointerException("NARG");
		
		// Check the access for the class first
		NCIClass otherouter = __ao.outerClass();
		if (!(__ao instanceof NCIClass))
			if (!canAccess(__from, otherouter))
				return false;
		
		// Target flags
		NCIAccessibleFlags af = __ao.flags();
		
		// If public, always OK
		if (af.isPublic())
			return true;
		
		// Get the name of our class and the object
		NCIClass myclass = __from.outerClass();
		ClassNameSymbol tname = myclass.thisName(),
			oname = otherouter.thisName();
		
		// If this is the same exact class then no checks have to be performed
		if (tname.equals(oname))
			return true;
		
		// Otherwise these are never accessible
		else if (af.isPrivate())
			return false;
		
		// Otherwise the package must match
		else if (af.isPackagePrivate())
			return tname.parentPackage().equals(oname.parentPackage());
		
		// The other class must be a super class or the same as this class
		else if (af.isProtected())
		{
			// Detect potential class recursion here
			Set<ClassNameSymbol> didclass = new HashSet<>();
			didclass.add(tname);
			
			// Go through super classes
			for (NCIClass rover = myclass;;)
			{
				// Go to the super class
				ClassNameSymbol scn = rover.superName();
				
				// If out of classes then it cannot be implemented
				if (scn == null)
					return false;
				
				// {@squirreljme.error AO09 The specified class eventually
				// extends itself. (The name of the current class)}
				if (didclass.contains(scn))
					throw new NCIException(NCIException.Issue.CIRCULAR_EXTENDS,
						String.format("AO09 %s", tname));
				
				// Go to that class
				rover = lookupClass(scn);
				
				// {@squirreljme.error AO0a Cannot check protected access
				// against a super class if it does not exist. (The name of the
				// super class)}
				if (rover == null)
					throw new NCIException(NCIException.Issue.MISSING_CLASS,
						String.format("AO0a %s", scn));
				
				// Same name?
				if (tname.equals(scn))
					return true;
			}
		}
		
		// {@squirreljme.error AO0b The accessible object to check access
		// against has an impossible flag combination. (The accessible object;
		// The accessible object flags)}
		else
			throw new NCIException(NCIException.Issue.ILLEGAL_ACCESS_FLAGS,
				String.format("AO0b %s", __ao, af));
	}
	
	/**
	 * Looks up a class name.
	 *
	 * @param __cn The name of the class.
	 * @return The discovered class by its given name or {@code null} if it
	 * was not found.
	 * @throws NCIException If the class exists however it is not
	 * formed correctly or could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public final NCIClass lookupClass(BinaryNameSymbol __cn)
		throws NCIException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Wrapped
		return lookupClass(__cn.asClassName());
	}
	
	/**
	 * Looks up a class name.
	 *
	 * @param __cn The name of the class.
	 * @return The discovered class by its given name or {@code null} if it
	 * was not found.
	 * @throws NCIException If the class exists however it is not
	 * formed correctly or could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public final NCIClass lookupClass(ClassNameSymbol __cn)
		throws NCIException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Lock on the cache
		Map<ClassNameSymbol, Reference<NCIClass>> cache = _loaded;
		synchronized (cache)
		{
			// Already exists?
			Reference<NCIClass> ref = cache.get(__cn);
			NCIClass rv;
			
			// Needs to be loaded?
			if (ref == null || null == (rv = ref.get()))
			{
				// Use precomposed synthetic class for primitive types
				if (__cn.isPrimitive())
					throw new Error("TODO");
				
				// Virtualize creation and representation of array types
				else if (__cn.isArray())
					throw new Error("TODO");
				
				// Otherwise lookup the class
				else
					rv = internalClassLookup(__cn.asBinaryName());
				
				// No class?
				if (rv == null)
					return null;
				
				// Cache it
				cache.put(__cn, new WeakReference<>(__verifyClass(rv)));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Looks up a method for a given class.
	 *
	 * @param __cn The containing class.
	 * @param __id The method identifier.
	 * @return The associated method or {@code null} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final NCIMethod lookupMethod(ClassNameSymbol __cn,
		NCIMethodID __id)
		throws NullPointerException
	{
		// Check
		if (__cn == null || __id == null)
			throw new NullPointerException("NARG");
		
		// Locate the class first
		NCIClass ncl = lookupClass(__cn);
		if (ncl == null)
			return null;
		
		// Obtain the given method
		return ncl.methods().get(__id);
	}
	
	/**
	 * Looks up a method for a given class.
	 *
	 * @param __cn The containing class.
	 * @param __mn The name of the method.
	 * @param __mt The type of the method.
	 * @return The associated method or {@code null} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final NCIMethod lookupMethod(ClassNameSymbol __cn,
		IdentifierSymbol __mn, MethodSymbol __mt)
		throws NullPointerException
	{
		return lookupMethod(__cn, new NCIMethodID(__mn, __mt));
	}
	
	/**
	 * Looks up a method for a given class.
	 *
	 * @param __ref The reference to the given method.
	 * @return The associated method or {@code null} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final NCIMethod lookupMethod(NCIMethodReference __ref)
		throws NullPointerException
	{
		// Check
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return lookupMethod(__ref.memberClass(), __ref.memberName(),
			__ref.memberType());
	}
	
	/**
	 * Verifies that the given class is correctly formed in that it does not
	 * extend itself or any final classes, and that any methods are correctly
	 * overridden and implemented as such.
	 *
	 * @param __cl The class to verify.
	 * @return {@code __cl} or a wrapped variant of it.
	 * @throws NCIException If the conditions do not hold and the class breaks
	 * the requirements set by the virtual machine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	private final NCIClass __verifyClass(NCIClass __cl)
		throws NCIException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		if (true)
			throw new Error("TODO");
		
		// Return the input (or perhaps a wrapped class in the future)
		return __cl;
	}
}

