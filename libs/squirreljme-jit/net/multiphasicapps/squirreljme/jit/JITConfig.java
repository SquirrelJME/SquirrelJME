// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.HashMap;
import java.util.Map;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @param <C> The configuration class.
 * @since 2017/02/02
 */
public abstract class JITConfig<C extends JITConfig<C>>
{
	/** Mapping of configuration values. */
	private final Map<String, String> _values;
	
	/**
	 * Initializes the JIT configuration.
	 *
	 * @param __kvp Key/value pairs to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public JITConfig(String... __kvp)
		throws NullPointerException
	{
		this(new __KVPMap__(__kvp));
	}
	
	/**
	 * Initializes the JIT configuration.
	 *
	 * @param __kvp Key/value pairs to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/02
	 */
	public JITConfig(Map<String, String> __kvp)
		throws NullPointerException
	{
		// Check
		if (__kvp == null)
			throw new NullPointerException("NARG");
		
		// Copy
		Map<String, String> to = new HashMap<>();
		for (Map.Entry<String, String> e : __kvp.entrySet())
			to.put(__lower(e.getKey()), __lower(e.getValue()));
		this._values = to;
	}
	
	/**
	 * Returns the class which is used to serialize and de-serialize the
	 * JIT.
	 *
	 * @return The serializer for this JIT configuration.
	 * @since 2017/02/01
	 */
	public abstract JITConfigSerializer<C> serializer();
	
	/**
	 * Obtains an internally set value.
	 *
	 * @param __s The key to get.
	 * @return The value of the given key or {@code null} if no value was set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/13
	 */
	protected final String internalValue(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		return this._values.get(__s);
	}
	
	/**
	 * Lowercases the given string.
	 *
	 * @param __s The string to lowercase, may be {@code null}.
	 * @return The lowercased string or {@code null} if it was {@code null}.
	 * @since 2017/02/02
	 */
	private static final String __lower(String __s)
	{
		// Do nothing with null
		if (__s == null)
			return null;
		
		// Check for capitals
		int i, n;
		for (i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			if (c >= 'A' && c <= 'Z')
				break;
		}
		
		// None
		if (i >= n)
			return __s;
		
		// Lowercase
		StringBuilder sb = new StringBuilder(n);
		if (i > 0)
			sb.append(__s.substring(0, i));
		
		// Lowercase characters
		for (; i < n; i++)
		{
			char c = __s.charAt(i);
			if (c >= 'A' && c <= 'Z')
				c = (char)('a' + (c - 'A'));
			sb.append(c);
		}
		
		// Use it
		return sb.toString();
	}
}

