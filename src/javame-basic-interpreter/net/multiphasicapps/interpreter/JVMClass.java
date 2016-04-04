// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.CFClass;
import net.multiphasicapps.classfile.CFClassFlag;
import net.multiphasicapps.classfile.CFClassFlags;
import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This represents a class which is loaded by the virtual machine.
 *
 * @since 2016/04/02
 */
public class JVMClass
{
	/** All arrays use the given flags. */
	public static final CFClassFlags ARRAY_FLAGS =
		new CFClassFlags(CFClassFlag.PUBLIC.mask |
			CFClassFlag.FINAL.mask |
			CFClassFlag.SYNTHETIC.mask);

	/** The owning engine. */
	protected final JVMEngine engine;
	
	/** The class to base off. */
	protected final CFClass basedon;
	
	/** Is this an array? */
	protected final boolean isarray;
	
	/** The name of this class. */
	protected final ClassNameSymbol classname;
	
	/** Class loader formatted binary name cache. */
	private volatile Reference<String> _clbname;
	
	/**
	 * Initializes a class which is based on the given class.
	 *
	 * @param __e The owning engine.
	 * @param __cf The parsed class file data.
	 * @since 2016/03/02
	 */
	public JVMClass(JVMEngine __e, CFClass __cf)
		throws NullPointerException
	{
		// Check
		if (__e == null || __cf == null)
			throw new NullPointerException("NARG");
		
		// Set
		engine = __e;
		basedon = __cf;
		classname = basedon.thisName().toClassName();
		
		// Not an array
		isarray = false;
	}
	
	/**
	 * Returns the name of this class as returned by {@link Class#getName()}.
	 *
	 * @return The name of this class.
	 * @throws IllegalStateException If the name of the class was not set.
	 * @since 2016/03/02
	 */
	public final String classLoaderName()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<String> ref = _clbname;
			String rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
			{
				// If an array, then the field syntax is directly used
				if (isArray())
					rv = thisName().toString();
				
				// Otherwise use the binary form instead but with characters
				// replaced
				else
					rv = thisName().toString().replace('/', '.');
				
				// Cache it
				_clbname = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the flags which are associated with the current class.
	 *
	 * @return The class flags.
	 * @since 2016/04/02
	 */
	public CFClassFlags flags()
	{
		// If an array, the flags are fixed to a specific variety.
		if (isarray)
			return ARRAY_FLAGS;
		
		// Otherwise defer
		return basedon.flags();
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2016/04/04
	 */
	public ClassNameSymbol thisName()
	{
		return classname;
	}
}

