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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

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
	
	/** String representation. */
	private volatile Reference<String> _string;
	
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
		
		return toString().equals(((JITConfigValue)__o).toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	/**
	 * Returns {@code true} if this is an integer value.
	 *
	 * @return If the given value is an integer.
	 * @since 2017/08/19
	 */
	public boolean isInteger()
	{
		// Attempt to decode a value
		try
		{
			Integer.decode(this.value);
			return true;
		}
		
		// Did not parse a value
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if this is a long value.
	 *
	 * @return If the given value is a long.
	 * @since 2017/08/19
	 */
	public boolean isLong()
	{
		// Attempt to decode a value
		try
		{
			Long.decode(this.value);
			return true;
		}
		
		// Did not parse a value
		catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * Returns the value as a boolean.
	 *
	 * @return The value as a boolean.
	 * @since 2017/08/10
	 */
	public boolean toBoolean()
	{
		return TRUE.equals(matchesTrue(this));
	}
	
	/**
	 * Parses this value as an int value.
	 *
	 * @return The parsed value.
	 * @throws NumberFormatException If the value could not be parsed.
	 * @since 2017/08/19
	 */
	public int toInteger()
		throws NumberFormatException
	{
		return Integer.decode(this.value);
	}
	
	/**
	 * Returns the value as an int or returns the default value.
	 *
	 * @param __d The default value if this is not parsable as an int.
	 * @return The parsed value or {@code __d}.
	 * @since 2017/08/19
	 */
	public int toInteger(int __d)
	{
		try
		{
			return toInteger();
		}
		catch (NumberFormatException e)
		{
			return __d;
		}
	}
	
	/**
	 * Parses this value as a long value.
	 *
	 * @return The parsed value.
	 * @throws NumberFormatException If the value could not be parsed.
	 * @since 2017/08/19
	 */
	public long toLong()
		throws NumberFormatException
	{
		return Long.decode(this.value);
	}
	
	/**
	 * Returns the value as a long or returns the default value.
	 *
	 * @param __d The default value if this is not parsable as a long.
	 * @return The parsed value or {@code __d}.
	 * @since 2017/08/19
	 */
	public long toLong(long __d)
	{
		try
		{
			return toLong();
		}
		catch (NumberFormatException e)
		{
			return __d;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/30
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
		{
			// Decode as a long value
			try
			{
				rv = Long.toString(toLong());
			}
			
			// Treat as string value
			catch (NumberFormatException e)
			{
				rv = this.value;
			}
			
			// Cache
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns a configuration value if the given value matches true.
	 *
	 * @param __v The value to check.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2017/08/10
	 */
	public static JITConfigValue matchesTrue(JITConfigValue __v)
	{
		if (__v == null)
			return FALSE;
		return matchesTrue(__v.toString());
	}
	
	/**
	 * Returns a configuration value if the given value matches true.
	 *
	 * @param __v The value to check.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2017/08/10
	 */
	public static JITConfigValue matchesTrue(String __v)
	{
		if (__v == null)
			return FALSE;
		return matchesTrue("true".equals(__v));
	}
	
	/**
	 * Returns a configuration value if the given value matches true.
	 *
	 * @param __v The value to check.
	 * @return Either {@link #TRUE} or {@link #FALSE}.
	 * @since 2017/08/10
	 */
	public static JITConfigValue matchesTrue(boolean __v)
	{
		if (!__v)
			return FALSE;
		return TRUE;
	}
}

