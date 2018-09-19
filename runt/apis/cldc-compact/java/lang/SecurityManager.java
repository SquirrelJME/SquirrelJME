// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.security.AccessController;
import java.security.Permission;
import java.util.PropertyPermission;

public class SecurityManager
{
	public SecurityManager()
	{
		throw new todo.TODO();
	}
	
	public void checkAccept(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public void checkAccess(Thread __a)
	{
		throw new todo.TODO();
	}
	
	public void checkConnect(String __a, int __b)
	{
		throw new todo.TODO();
	}
	
	public void checkDelete(String __a)
	{
		throw new todo.TODO();
	}
	
	public void checkExit(int __a)
	{
		throw new todo.TODO();
	}
	
	public void checkListen(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Checks whether the given permission is permitted.
	 *
	 * @param __p The permission to check.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If permission is denied.
	 * @since 2018/09/18
	 */
	public void checkPermission(Permission __p)
		throws NullPointerException, SecurityException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		AccessController.checkPermission(__p);
	}
	
	/**
	 * Checks if the given system property can be accessed.
	 *
	 * @param __key The key to check.
	 * @throws IllegalArgumentException If the key is empty.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If access to the property is denied.
	 * @since 2018/09/18
	 */
	public void checkPropertyAccess(String __key)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0y Request to check access to system property
		// with an empty key.}
		if (__key.isEmpty())
			throw new IllegalArgumentException("ZZ0y");
		
		// Forward
		this.checkPermission(new PropertyPermission(__key, "read"));
	}
	
	public void checkRead(String __a)
	{
		throw new todo.TODO();
	}
	
	public void checkWrite(String __a)
	{
		throw new todo.TODO();
	}
}

