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
import java.util.Map;
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
	implements JVMComponentType
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
		classname = basedon.thisName().asClassName();
		
		// Not an array
		isarray = false;
	}
	
	/**
	 * Initializes a class which is a component of the given class.
	 *
	 * @param __e The owning engine.
	 * @param __comp The component type of the array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/04
	 */
	public JVMClass(JVMEngine __e, JVMComponentType __comp)
		throws NullPointerException
	{
		// Check
		if (__e == null || __comp == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the compiler representation of the class that this is based on.
	 *
	 * @return The class file, or {@code null} if an array.
	 * @since 2016/04/04
	 */
	public final CFClass base()
	{
		if (isarray)
			return null;
		return basedon;
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
		synchronized (this)
		{
			// Get reference
			Reference<String> ref = _clbname;
			String rv;
			
			// Needs to be cached?
			if (ref == null || null == (rv = ref.get()))
			{
				// If an array, then the field syntax is directly used
				if (isarray)
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
	 * Returns the list of fields that this class contains.
	 *
	 * @return The fields contained in this class.
	 * @since 2016/04/04
	 */
	public JVMFields fields()
	{
		throw new Error("TODO");
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
	 * Returns the list of methods that this class contains.
	 *
	 * @return The methods contained in this class.
	 * @since 2016/04/04
	 */
	public JVMMethods methods()
	{
		throw new Error("TODO");
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

