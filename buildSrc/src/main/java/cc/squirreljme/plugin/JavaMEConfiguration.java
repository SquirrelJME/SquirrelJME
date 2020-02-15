// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

/**
 * Defines a Java ME Configuration.
 *
 * @since 2020/02/15
 */
public final class JavaMEConfiguration
{
	/** The configuration string. */
	protected final String string;
	
	/**
	 * Defines full configuration.
	 *
	 * @param __full The full string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	public JavaMEConfiguration(String __full)
		throws NullPointerException
	{
		if (__full == null)
			throw new NullPointerException("No configuration specified");
		
		this.string = __full;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JavaMEConfiguration))
			return false;
		
		return this.string.equals(((JavaMEConfiguration)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public final String toString()
	{
		return this.string;
	}
}

