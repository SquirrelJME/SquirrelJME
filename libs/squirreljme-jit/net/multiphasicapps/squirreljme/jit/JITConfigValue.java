// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
	/** True. */
	public static final JITConfigValue TRUE =
		new JITConfigValue("true");
	
	/** False. */
	public static final JITConfigValue FALSE =
		new JITConfigValue("false");
	
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
		
		// Lowercase value
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
	
	/**
	 * Returns a configuration value if the given value matches true.
	 *
	 * @param __v The value to check.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2017/08/10
	 */
	public static JITConfigValue matchTrue(JITConfigValue __v)
	{
		if (__v == null)
			return FALSE;
		return matchTrue(__v.toString());
	}
	
	/**
	 * Returns a configuration value if the given value matches true.
	 *
	 * @param __v The value to check.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2017/08/10
	 */
	public static JITConfigValue matchTrue(String __v)
	{
		if (__v == null)
			return FALSE;
		return matchTrue("true".equals(__v));
	}
	
	/**
	 * Returns a configuration value if the given value matches true.
	 *
	 * @param __v The value to check.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2017/08/10
	 */
	public static JITConfigValue matchTrue(boolean __v)
	{
		if (!__v)
			return FALSE;
		return TRUE;
	}
}

