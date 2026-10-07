// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is the base class for all permission types, in SquirrelJME this only
 * exists for compatibility purposes.
 *
 * Permissions have a name and may have multiple actions.
 *
 * Actions are comma separated, they must be returned in a fixed order.
 *
 * @see SecurityManager
 * @since 2018/12/08
 */
@Api
@ApiDefinedDeprecated
@KeepAbsolutelyEverything("Lightweight 'Security' permission system.")
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
	@Api
	@ApiDefinedDeprecated
	public Permission(String __name)
	{
		this._name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	@ApiDefinedDeprecated
	public abstract boolean equals(Object __a);
	
	/**
	 * Returns the actions which are performed on this permission.
	 *
	 * @return The actions performed on this permission.
	 * @since 2018/12/08
	 */
	@Api
	@ApiDefinedDeprecated
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
	@Api
	@ApiDefinedDeprecated
	public abstract boolean implies(Permission __p);
	
	/**
	 * Returns the name of this permission.
	 *
	 * @return The permission name.
	 * @since 2018/12/08
	 */
	@Api
	@ApiDefinedDeprecated
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
	@Api
	@ApiDefinedDeprecated
	public PermissionCollection newPermissionCollection()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Api
	@Override
	@ApiDefinedDeprecated
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

