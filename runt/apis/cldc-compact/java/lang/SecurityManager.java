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
	/** The current security manager, defaults to the system one. */
	static volatile SecurityManager _CURRENT_MANAGER =
		new SecurityManager();
	
	/**
	 * Initializes the security manager, if a security manager already exists
	 * then the
	 *
	 * @throws SecurityException If the manager could not be created.
	 * @since 2018/09/18 
	 */
	public SecurityManager()
		throws SecurityException
	{
		// Lock on this class, since multiple threads cannot mess around with
		// this check
		synchronized (SecurityManager.class)
		{
			// If one already exists, check to see if it can be created first
			SecurityManager current = SecurityManager._CURRENT_MANAGER;
			if (current != null)
				current.checkPermission(
					new RuntimePermission("createSecurityManager"));
		}
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
	
	/**
	 * Checks that the virtual machine can exit with the given code.
	 *
	 * @param __code The exit code.
	 * @throws SecurityException If exit is not permitted.
	 * @since 2018/10/13
	 */
	public void checkExit(int __code)
		throws SecurityException
	{
		this.checkPermission(new RuntimePermission("exitVM." + __code));
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

