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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.Permission;
import java.security.PermissionCollection;

/**
 * This represents a permission for the trust manager.
 *
 * @since 2018/01/31
 */
@Deprecated
public final class TrustPermission
	extends Permission
{
	/** Get trust ID. */
	@Deprecated
	public static final String ACTION_CHECK_TRUST_ID =
		"check-trust-id";
	
	/** The untrusted action. */
	@Deprecated
	public static final String ACTION_GET_UNTRUSTED =
		"get-untrusted";
	
	/**
	 * Initializes the trust permission.
	 *
	 * @param __name The {@code name:vendor} pair.
	 * @param __actions The actions for the permission.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	@Deprecated
	public TrustPermission(String __name, String __actions)
		throws NullPointerException
	{
		super(__name);
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Deprecated
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Deprecated
	@Override
	public String getActions()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Deprecated
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Deprecated
	@Override
	public boolean implies(Permission __p)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Deprecated
	@Override
	public PermissionCollection newPermissionCollection()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Deprecated
	@Override
	public String toString()
	{
		throw new todo.TODO();
	}
}

