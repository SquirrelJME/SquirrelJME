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
 * This represents a group which tasks run under and which libraries are
 * installed under and represent groups they are installed within.
 *
 * @since 2018/01/09
 */
public interface SystemTrustGroup
{
	/**
	 * Checks that the specified permission is valid.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/11
	 */
	public abstract void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException;
	
	/**
	 * Compares this trust group against another object. The method
	 * {@link SystemTrustGroupUtils#equals(SystemTrustGroup, SystemTrustGroup)}
	 * may be used to compare.
	 *
	 * @param __o The object to compare against.
	 * @return If the trust group is equal to another.
	 * @since 2018/02/12
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * Returns the hash code of this trust group. The method
	 * {@link SystemTrustGroupUtils#hashCode(SystemTrustGroup)}
	 * may be used to generate it.
	 *
	 * @return The hash code for this group.
	 * @since 2018/02/12
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Returns the index of the trust group.
	 *
	 * @return The trust group index.
	 * @since 2018/01/11
	 */
	public abstract int index();
	
	/**
	 * Is this trust group trusted?
	 *
	 * @return If the group is a trusted group.
	 * @since 2018/01/31
	 */
	public abstract boolean isTrusted();
	
	/**
	 * The group name.
	 *
	 * @return The name of the group.
	 * @since 2018/01/31
	 */
	public abstract String name();
	
	/**
	 * The vendor name.
	 *
	 * @return The vendor of the group.
	 * @since 2018/01/31
	 */
	public abstract String vendor();
}

