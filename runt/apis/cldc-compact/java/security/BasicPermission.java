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

public abstract class BasicPermission
	extends Permission
{
	/**
	 * Initializes the basic permission.
	 *
	 * @param __name The name of the permission.
	 * @throws IllegalArgumentException If name is empty.
	 * @throws NullPointerException If no name was specified.
	 * @since 2018/09/18
	 */
	public BasicPermission(String __name)
		throws IllegalArgumentException, NullPointerException
	{
		super(__name);
		
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0e The name for basic permissions cannot
		// be empty.}
		if (__name.equals(""))
			throw new IllegalArgumentException("ZZ0z");
	}
	
	/**
	 * Initializes the basic permission.
	 *
	 * @param __name The name of the permission.
	 * @param __act The action to use, this is ignored for basic permissions.
	 * @throws IllegalArgumentException If name is empty.
	 * @throws NullPointerException If no name was specified.
	 * @since 2018/09/18
	 */
	public BasicPermission(String __name, String __act)
		throws IllegalArgumentException, NullPointerException
	{
		this(__name);
	}
	
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	public String getActions()
	{
		throw new todo.TODO();
	}
	
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	public boolean implies(Permission __a)
	{
		throw new todo.TODO();
	}
	
	public PermissionCollection newPermissionCollection()
	{
		throw new todo.TODO();
	}
}

