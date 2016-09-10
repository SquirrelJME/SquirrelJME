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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

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
	
	/** The configuration properties. */
	protected final Map<String, String> properties;
	
	/** Key as a given instance of a given class cache. */
	private final Map<String, Reference<Object>> _keyasclass =
		new HashMap<>();
	
	/** The output factory. */
	@Deprecated
	private volatile Reference<JITOutputFactory> _factory;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/** The serialized representation. */
	private volatile Reference<String> _serial;
	
	/** The triplet cache. */
	private volatile Reference<JITTriplet> _triplet;
	
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
		
		// Copy propertyes
		this.properties = UnmodifiableMap.<String, String>of(
			new LinkedHashMap<>(__b._properties));
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
	 * Attempts to parse the value of the given pr
	 *
	 * @param <Q> The type of class to the key value as.
	 * @param __k The key to treat as a class name.
	 * @param __cl The class type to get the key as.
	 * @throws JITException If the value is {@code null} or is not of the
	 * given class type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/10
	 */
	public final <Q> Q getAsClass(String __k, Class<Q> __cl)
		throws JITException, NullPointerException
	{
		// Check
		if (__k == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Map<String, Reference<Object>> keyasclass = this._keyasclass;
		synchronized (keyasclass)
		{
			throw new Error("TODO");
		}
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
		
		// Get
		return this.properties.get(__k);
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
		// Get
		Reference<JITOutputFactory> ref = this._factory;
		JITOutputFactory rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			try
			{
				// {@squirreljme.error ED05 The JIT output factory has not been
				// set within the configuration. (The configuration)}
				String prop = getProperty(FACTORY_PROPERTY);
				if (prop == null)
					throw new JITException(String.format("ED05 %s", this));
				
				// Create instance
				Class<?> cl = Class.forName(prop);
				rv = (JITOutputFactory)cl.newInstance();
				
				// Store
				this._factory = new WeakReference<>(rv);
			}
			
			// {@squirreljme.error ED06 Could not initialize the output factory
			// used for JIT compilation. (This configuration)}
			catch (ClassNotFoundException|InstantiationException|
				IllegalAccessException e)
			{
				throw new JITException(String.format("ED06 %s", this), e);
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
		// Get
		Reference<String> ref = this._serial;
		String rv;
		
		// Serialize it?
		if (ref == null || null == (rv = ref.get()))
			throw new Error("TODO");
		
		// Return it
		return rv;
	}
	
	/**
	 * {@inheritDoc]
	 * @since 2016/09/10
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				this.properties.toString()));
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the triplet used in the given configuration.
	 *
	 * @return The target triplet.
	 * @throws JITException If no triplet was specified or it is not valid.
	 * @since 2016/09/10
	 */
	public final JITTriplet triplet()
		throws JITException
	{
		// Get
		Reference<JITTriplet> ref = this._triplet;
		JITTriplet rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// {@squirreljme.error ED07 The triplet has not been specified in
			// the configuration. (This configuration)}
			String prop = getProperty(TRIPLET_PROPERTY);
			if (prop == null)
				throw new JITException(String.format("ED07 %s", this));
			
			// Create it
			this._triplet = new WeakReference<>((rv = new JITTriplet(prop)));
		}
		
		// Return it
		return rv;
	}
}

