// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.library;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This is the base class which provides access to classes as an entire
 * library. This is used by the native compile to look at the details of a
 * class to determine how the code should be compiled.
 *
 * @since 2016/04/20
 */
public abstract class NLClassLibrary
{
	/** The loaded class library. */
	private final Map<BinaryNameSymbol, Reference<NLClass>> _loaded =
		new HashMap<>();
	
	/**
	 * Initializes the class library.
	 *
	 * @since 2016/04/20
	 */
	public NLClassLibrary()
	{
	}
	
	/**
	 * Loads the class with the given name.
	 *
	 * @param __bn The binary name of class.
	 * @return The class with the given or {@code null} if it does not exist.
	 * @throws NLClassLoadException If the class exists however it is not
	 * formed correctly or could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	protected abstract NLClass loadClass(BinaryNameSymbol __bn)
		throws NLClassLoadException, NullPointerException;
	
	/**
	 * Looks up a binary name.
	 *
	 * @param __bn The binary name of the class.
	 * @return The discovered class by its given name or {@code null} if it
	 * was not found.
	 * @throws NLClassLoadException If the class exists however it is not
	 * formed correctly or could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public final NLClass lookup(BinaryNameSymbol __bn)
		throws NLClassLoadException, NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Lock on the cache
		Map<BinaryNameSymbol, Reference<NLClass>> cache = _loaded;
		synchronized (cache)
		{
			// Already exists?
			Reference<NLClass> ref = cache.get(__bn);
			NLClass rv;
			
			// Needs to be loaded?
			if (ref == null || null == (rv = ref.get()))
			{
				// Load the class
				rv = loadClass(__bn);
				
				// No class?
				if (rv == null)
					return null;
				
				// Cache it
				cache.put(__bn, new WeakReference<>(rv));
			}
			
			// Return it
			return rv;
		}
	}
}

