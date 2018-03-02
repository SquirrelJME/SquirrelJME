// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.trust;

/**
 * Utilities for system trust groups.
 *
 * @since 2018/02/12
 */
public final class SystemTrustGroupUtils
{
	/**
	 * Not used.
	 *
	 * @since 2018/02/12
	 */
	private SystemTrustGroupUtils()
	{
	}
	
	/**
	 * Checks if the two given trust groups are equal.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return {@code true} if they are equal.
	 * @since 2018/02/12
	 */
	public static final boolean equals(SystemTrustGroup __a,
		SystemTrustGroup __b)
	{
		// Exactly the same
		if (__a == __b)
			return true;
		
		// One is null, the other is not
		if ((__a == null) != (__b == null))
			return false;
		
		// Both null
		if (__a == null)
			return true;
		
		return __a.index() == __b.index() &&
			__a.isTrusted() == __b.isTrusted() &&
			__a.name().equals(__b.name()) &&
			__a.vendor().equals(__b.vendor());
	}
	
	/**
	 * Returns the hash code for this trust group.
	 *
	 * @param __a The group to get the hashcode for.
	 * @return The hash code for the group.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/12
	 */
	public static final int hashCode(SystemTrustGroup __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		return __a.index() ^
			(__a.isTrusted() ? ~0 : 0) ^
			__a.name().hashCode() ^
			__a.vendor().hashCode();
	}
}

