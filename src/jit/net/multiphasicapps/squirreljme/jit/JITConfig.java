// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This represents and stores the configuration which is used to configure
 * how the JIT generates code.
 *
 * This class is serialized to a single string which is then stored in a
 * system property of the resultant executable so that at run-time the JIT may
 * be reconfigured to generate code for a target without requiring assistance.
 *
 * {@squirreljme.property net.multiphasicapps.squirreljme.jit.factory=(class)
 * This specifies an implementation of {@link JITOutputFactory} which is used
 * to generate the actual JIT code.}
 *
 * @since 2016/09/10
 */
public final class JITConfig
{
	/** The property which defines the target triplet. */
	public static final String TRIPLET_PROPERTY =
		"net.multiphasicapps.squirreljme.jit.triplet";
	
	/** The factory instance to use when performing a compile. */
	public static final String FACTORY_PROPERTY =
		"net.multiphasicapps.squirreljme.jit.factory";
	
	/** The output factory. */
	private volatile Reference<JITOutputFactory> _factory;
	
	/**
	 * Initializes the configuration from the given builder.
	 *
	 * @param __b The builder to get information from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	JITConfig(JITConfigBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This deserializes the JIT configuration from the given string and
	 * re-initializes any required fields from it.
	 *
	 * @param __s The string to deserialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public JITConfig(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns a property that was set in the configuration.
	 *
	 * @return The property key to get the value for or {@code null} if it has
	 * not been set.
	 * @throws NUllPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public final String getProperty(String __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the output factory that is used to generate code through the
	 * JIT system.
	 *
	 * @return The JIT factory instance.
	 * @throws JITException If the property was not set or the factory could
	 * not be initialized.
	 * @since 2016/09/10
	 */
	public final JITOutputFactory outputFactory()
		throws JITException
	{
		// {@squirreljme.error ED05 The JIT output factory has not been set
		// within the configuration.}
		String prop = getProperty(FACTORY_PROPERTY);
		if (prop == null)
			throw new JITException("ED05");
		
		// Get
		Reference<JITOutputFactory> ref = this._factory;
		JITOutputFactory rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			try
			{
				// Create instance
				Class<?> cl = Class.forName(prop);
				rv = (JITOutputFactory)cl.newInstance();
				
				// Store
				this._factory = new WeakReference<>(rv);
			}
			
			// {@squirreljme.error ED06 Could not initialize the output factory
			// used for JIT compilation.}
			catch (ClassNotFoundException|InstantiationException|
				IllegalAccessException e)
			{
				throw new JITException("ED06", e);
			}
		
		// Return it
		return rv;
	}
	
	/**
	 * Serializes this configuration so that it may be stored within a system
	 * property.
	 *
	 * @return The serialized form of the current configuration.
	 * @since 2016/09/10
	 */
	public final String serialize()
	{
		throw new Error("TODO");
	}
}

