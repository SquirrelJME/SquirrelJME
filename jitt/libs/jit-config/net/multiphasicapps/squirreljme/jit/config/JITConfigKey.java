// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.config;

/**
 * This is used as a key for {@link JITConfig} which makes the keys case
 * insensitive.
 *
 * @since 2017/05/30
 */
public final class JITConfigKey
	implements Comparable<JITConfigKey>
{
	/** The key name. */
	protected final String key;
	
	/**
	 * Initializes the key.
	 *
	 * @param __s The string used for the key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/30
	 */
	public JITConfigKey(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lowercase key
		StringBuilder sb = new StringBuilder();
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			if (c >= 'A' && c <= 'Z')
				c = (char)((c - 'A') + 'a');
			
			sb.append(c);
		}
		this.key = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public int compareTo(JITConfigKey __o)
	{
		return this.key.compareTo(__o.key);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof JITConfigKey))
			return false;
		
		return 0 == compareTo((JITConfigKey)__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public int hashCode()
	{
		return this.key.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public String toString()
	{
		return this.key;
	}
}

