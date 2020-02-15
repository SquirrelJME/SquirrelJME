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
 * Represents a Java ME Profile.
 *
 * @since 2020/02/15
 */
public final class JavaMEProfile
{
	/** The profile string. */
	protected final String string;
	
	/**
	 * Defines full profile.
	 *
	 * @param __full The full string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	public JavaMEProfile(String __full)
		throws NullPointerException
	{
		if (__full == null)
			throw new NullPointerException("No profile specified");
		
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
		
		if (!(__o instanceof JavaMEProfile))
			return false;
		
		return this.string.equals(((JavaMEProfile)__o).string);
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

