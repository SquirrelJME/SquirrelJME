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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIAccessibleFlags;
import net.multiphasicapps.squirreljme.ci.CIAccessibleObject;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIException;

/**
 * This is a class path, it contains multiple {@link ClassUnit}s and provides
 * a uniform view of the classes and resources which are available to a
 * program.
 *
 * @since 2016/05/25
 */
public final class ClassPath
{
	/** The class unitss which are available for usage. */
	private final ClassUnit[] _units;
	
	/** The cache of classes which have already been loaded. */
	private final Map<ClassNameSymbol, Reference<CIClass>> _cache =
		new HashMap<>();
	
	/**
	 * The queue of classes which have loaded properly but have yet to be
	 * properly verified.
	 */
	private final Map<ClassNameSymbol, CIClass> _inverif =
		new HashMap<>();
	
	/**
	 * Initializes the class path using the given class units to
	 * locate classes and resources.
	 *
	 * @param __cus The class units to use.
	 * @throw NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public ClassPath(ClassUnit... __cus)
		throws NullPointerException
	{
		// Check
		if (__cus == null)
			throw new NullPointerException("NARG");
		
		// Set
		__cus = __cus.clone();
		this._units = __cus;
		for (ClassUnit cup : __cus)
			if (cup == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Checks whether the current byte code (the method that contains this byte
	 * code) can access the specified accessible object.
	 *
	 * @param __from The object to act as the check source.
	 * @param __ao The object to check access against.
	 * @return {@code true} if the object can be accessed.
	 * @throws CIException If the class has no set access flag type or is
	 * a class which eventually extends itself.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public final boolean canAccess(CIAccessibleObject __from,
		CIAccessibleObject __ao)
		throws CIException, NullPointerException
	{
		// Check
		if (__from == null || __ao == null)
			throw new NullPointerException("NARG");
		
		// Check the access for the class first
		CIClass otherouter = __ao.outerClass();
		if (!(__ao instanceof CIClass))
			if (!canAccess(__from, otherouter))
				return false;
		
		// Target flags
		CIAccessibleFlags af = __ao.flags();
		
		// If public, always OK
		if (af.isPublic())
			return true;
		
		// Get the name of our class and the object
		CIClass myclass = __from.outerClass();
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
			for (CIClass rover = myclass;;)
			{
				// Go to the super class
				ClassNameSymbol scn = rover.superName();
				
				// If out of classes then it cannot be implemented
				if (scn == null)
					return false;
				
				// {@squirreljme.error BN0g The specified class eventually
				// extends itself. (The name of the current class)}
				if (didclass.contains(scn))
					throw new CIException(String.format("BN0g %s", tname));
				
				// Go to that class
				rover = locateClass(scn);
				
				// {@squirreljme.error BN0h Cannot check protected access
				// against a super class if it does not exist. (The name of the
				// super class)}
				if (rover == null)
					throw new CIException(String.format("BN0h %s", scn));
				
				// Same name?
				if (tname.equals(scn))
					return true;
			}
		}
		
		// {@squirreljme.error BN0i The accessible object to check access
		// against has an impossible flag combination. (The accessible object;
		// The accessible object flags)}
		else
			throw new CIException(String.format("BN0i %s", __ao, af));
	}
	
	/**
	 * Returns the number of class units which are used.
	 *
	 * @return The number of class units.
	 * @since 2016/06/03
	 */
	public final int count()
	{
		return this._units.length;
	}
	
	/**
	 * Locates the given class by the specified name and verifies that it is
	 * well formed before it is returned.
	 *
	 * @param __cns The name of the class to locate.
	 * @return The located class.
	 * @throws CIException If the class could not be found or it could not
	 * properly be loaded.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/27
	 */
	public final CIClass locateClass(ClassNameSymbol __cns)
		throws CIException, NullPointerException
	{
		// Check
		if (__cns == null)
			throw new NullPointerException("NARG");
		
		// Lock
		ClassUnit[] units = this._units;
		synchronized (units)
		{
			// Get the cache
			Map<ClassNameSymbol, Reference<CIClass>> cache = this._cache;
			Reference<CIClass> ref = cache.get(__cns);
			CIClass rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
			{
				// Check if the class is in the verification stage (it has
				// already been partially loaded)
				Map<ClassNameSymbol, CIClass> inverif = this._inverif;
				rv = inverif.get(__cns);
				
				// In verification step, so return it
				if (rv != null)
					return null;
				
				// Load the class to be returned from the first unit that
				// has it.
				int n = units.length;
				for (int i = 0; i < n; i++)
					if (null != (rv = units[i].locateClass(__cns)))
						break;
				
				// {@squirreljme.error BN01 The specified class does not exist
				// in any class unit. (The name of the class)}
				if (__cns == null)
					throw new CIException(String.format("BN01 %s", __cns));
				
				// Is only in the verification stage temporarily
				try
				{
					// Add to classes being verified
					inverif.put(__cns, rv);
					
					// Verifiy
					new __Verifier__(this, rv);
				}
				
				// Remove from the verification regardless
				finally
				{
					inverif.remove(__cns);
				}
			}
			
			// Return the already verified class
			return rv;
		}
	}
	
	/**
	 * Locates the given class unit.
	 *
	 * @param __un The unit to locate.
	 * @return The found class unit or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final ClassUnit locateUnit(String __un)
		throws NullPointerException
	{
		// Check
		if (__un == null)
			throw new NullPointerException("NARG");
		
		// Go through units
		ClassUnit[] units = this._units;
		synchronized (units)
		{
			for (ClassUnit cu : units)
				if (cu.equals(__un))
					return cu;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Returns an array containing the class units which are used in this
	 * class path.
	 *
	 * @return The copy of class units which this class uses.
	 * @since 2016/05/30
	 */
	public final ClassUnit[] units()
	{
		ClassUnit[] units = this._units;
		synchronized (units)
		{
			return units.clone();
		}
	}
}

