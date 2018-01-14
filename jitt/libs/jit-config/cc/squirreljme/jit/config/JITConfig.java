// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.config;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @since 2017/05/29
 */
public final class JITConfig
{
	/** Values stored within the configuration, untranslated. */
	private final Map<JITConfigKey, JITConfigValue> _values =
		new SortedTreeMap<>();
	
	/** String representation of this configuration. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the JIT configuration using the given option set.
	 *
	 * @param __o The options used for the JIT.
	 * @throws JITException If the options are not valid.
	 * @throws NullPointerException On null arguments or if the option map
	 * contains a null value.
	 * @since 2017/05/30
	 */
	public JITConfig(Map<JITConfigKey, JITConfigValue> __o)
		throws JITException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Copy all options, nulls are not permitted
		Map<JITConfigKey, JITConfigValue> values = this._values;
		for (Map.Entry<JITConfigKey, JITConfigValue> e : __o.entrySet())
		{
			JITConfigKey k = e.getKey();
			JITConfigValue v = e.getValue();
			
			// Cannot be null
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			values.put(k, v);
		}
	}
	
	/**
	 * Obtains the value for the given key.
	 *
	 * @param __k The key to get.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public final JITConfigValue get(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return this._values.get(__k);
	}
	
	/**
	 * Obtains the value for the given key or returns a default value.
	 *
	 * @param __k The key to get.
	 * @parma __d If the key is not set then this value is returned.
	 * @return The value for the key or {@code __d}.
	 * @throws NullPointerException If no key was specified.
	 * @since 2017/08/29
	 */
	public final JITConfigValue get(JITConfigKey __k, JITConfigValue __d)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		Map<JITConfigKey, JITConfigValue> values = this._values;
		if (!values.containsKey(__k))
			return __d;
		return values.get(__k);
	}
	
	/**
	 * Obtains the value for the given key.
	 *
	 * @param __k The key to get.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public final boolean getBoolean(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Get
		JITConfigValue rv = get(__k);
		if (rv == null)
			return false;
		return rv.toBoolean();
	}
	
	/**
	 * Returns the int value of the specified key.
	 *
	 * @param __k The key to get the value of.
	 * @return The value of the given key.
	 * @throws NumberFormatException If the value could not be parsed.
	 * @since 2017/08/19
	 */
	public final int getInteger(JITConfigKey __k)
		throws NumberFormatException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AL01 The specified key has no assigned
		// value. (The key)}
		JITConfigValue rv = get(__k);
		if (rv == null)
			throw new NumberFormatException(String.format("AL01 %s", __k));
		return rv.toInteger();
	}
	
	/**
	 * Obtains a int value or returns a default value.
	 *
	 * @param __k The key to get.
	 * @param __def The default value to return if the value is not parsed as
	 * the requested type.
	 * @return The decoded value or {@code __def}.
	 * @since 2017/08/19
	 */
	public final int getInteger(JITConfigKey __k, int __def)
	{
		// Could fail
		try
		{
			return getInteger(__k);
		}
		
		// Not a number
		catch (NumberFormatException e)
		{
			return __def;
		}
	}
	
	/**
	 * Returns the long value of the specified key.
	 *
	 * @param __k The key to get the value of.
	 * @return The value of the given key.
	 * @throws NumberFormatException If the value could not be parsed.
	 * @since 2017/08/19
	 */
	public final long getLong(JITConfigKey __k)
		throws NumberFormatException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AL02 The specified key is null. (The key)}
		JITConfigValue rv = get(__k);
		if (rv == null)
			throw new NumberFormatException(String.format("AL02 %s", __k));
		return rv.toLong();
	}
	
	/**
	 * Obtains a long value or returns a default value.
	 *
	 * @param __k The key to get.
	 * @param __def The default value to return if the value is not parsed as
	 * the requested type.
	 * @return The decoded value or {@code __def}.
	 * @since 2017/08/19
	 */
	public final long getLong(JITConfigKey __k, long __def)
	{
		// Could fail
		try
		{
			return getLong(__k);
		}
		
		// Not a number
		catch (NumberFormatException e)
		{
			return __def;
		}
	}
	
	/**
	 * Obtains the value for the given key.
	 *
	 * @param __k The key to get.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public final String getString(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Get
		JITConfigValue rv = get(__k);
		if (rv == null)
			return "null";
		return rv.toString();
	}
	
	/**
	 * Returns a copy of the JIT configuration options.
	 *
	 * @return A copy of the JIT configuration options.
	 * @since 2017/08/09
	 */
	public final Map<JITConfigKey, JITConfigValue> options()
	{
		return new LinkedHashMap<>(this._values);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/10
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this._values.toString()));
		
		return rv;
	}
}

