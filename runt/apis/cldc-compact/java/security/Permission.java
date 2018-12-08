// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is the base class for all permissions.
 *
 * Permissions have a name and may have multiple actions.
 *
 * Actions are comma separated and they must be returned in a fixed order.
 *
 * @since 2018/12/08
 */
public abstract class Permission
{
	/** The permission name. */
	private final String _name;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the base permission.
	 *
	 * @param __name The name of the permission.
	 * @since 2018/09/18
	 */
	public Permission(String __name)
	{
		this._name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public abstract boolean equals(Object __a);
	
	/**
	 * Returns the actions which are performed on this permission.
	 *
	 * @return The actions performed on this permission.
	 * @since 2018/12/08
	 */
	public abstract String getActions();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * Checks if this permission implies the given permission.
	 *
	 * @param __p The other permission to check.
	 * @return If this permission implies the specified one.
	 * @since 2018/12/08
	 */
	public abstract boolean implies(Permission __p);
	
	/**
	 * Returns the name of this permission.
	 *
	 * @return The permission name.
	 * @since 2018/12/08
	 */
	public final String getName()
	{
		return this._name;
	}
	
	/**
	 * Returns an empty permission collection for this given permission or
	 * {@code null} if one is not defined. This collection may be used by
	 * permission implementation to check if there are any implied
	 * permissions via {@link #implies(Permission)}. If {@code null} is
	 * returned this means the caller may store this within any collection
	 * of permissions.
	 *
	 * The default implementation returns {@code null}.
	 *
	 * @return The permission collection.
	 * @since 2018/12/08
	 */
	public PermissionCollection newPermissionCollection()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
			"(\"" + this.getClass().getName() + "\" \"" + this._name + "\")"));
		
		return rv;
	}
}

