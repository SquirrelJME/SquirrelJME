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
import java.util.Map;
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
	protected abstract NCIClass loadClass(BinaryNameSymbol __bn)
		throws NCIException, NullPointerException;
	
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
	public final NCIClass lookup(BinaryNameSymbol __cn)
		throws NCIException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Wrapped
		return lookup(__cn.asClassName());
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
	public final NCIClass lookup(ClassNameSymbol __cn)
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
					rv = loadClass(__cn.asBinaryName());
				
				// No class?
				if (rv == null)
					return null;
				
				// Cache it
				cache.put(__cn, new WeakReference<>(rv));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Looks up a method for a given class.
	 *
	 * @param __virt If {@code true} then
	 * @param __cn The containing class.
	 * @param __mn The name of the method.
	 * @param __mt The type of the method.
	 * @return The associated method or {@code null} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final NCIMethod lookupMethod(boolean __virt, ClassNameSymbol __cn,
		IdentifierSymbol __mn, MethodSymbol __mt)
		throws NullPointerException
	{
		// Check
		if (__cn == null || __mn == null || __mt == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Looks up a method for a given class.
	 *
	 * @param __virt If {@code true} then
	 * @param __ref The reference to the given method.
	 * @return The associated method or {@code null} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/14
	 */
	public final NCIMethod lookupMethod(boolean __virt,
		NCIMethodReference __ref)
		throws NullPointerException
	{
		// Check
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		// Forward
		return lookupMethod(__virt, __ref.memberClass(), __ref.memberName(),
			__ref.memberType());
	}
}

