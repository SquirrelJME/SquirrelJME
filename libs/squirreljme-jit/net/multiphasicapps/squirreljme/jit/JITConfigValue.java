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

/**
 * This contains the values associated with keys in the JIT configuration.
 *
 * @since 2017/05/30
 */
public final class JITConfigValue
{
	/** The value of the option. */
	protected final String value;
	
	/**
	 * Initializes the value.
	 *
	 * @param __s The string used for the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/30
	 */
	public JITConfigValue(String __s)
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
		this.value = sb.toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof JITConfigValue))
			return false;
		
		return this.value.equals(((JITConfigValue)__o).value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public int hashCode()
	{
		return this.value.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public String toString()
	{
		return this.value;
	}
}

