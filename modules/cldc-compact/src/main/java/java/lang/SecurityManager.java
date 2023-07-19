// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.security.AccessController;
import java.security.Permission;
import java.util.PropertyPermission;

/**
 * This is the security manager which controls access to various functions of
 * the virtual machine.
 * 
 * Access is checked by {@link AccessController#checkPermission(Permission)}. 
 * 
 * @since 2020/07/02
 */
@Api
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
	@Api
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
	
	@Api
	public void checkAccept(String __a, int __b)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Checks if the given thread may be modified.
	 *
	 * @param __t The thread to check.
	 * @throws SecurityException If threads cannot be modified.
	 * @since 2018/11/21
	 */
	@Api
	public void checkAccess(Thread __t)
		throws SecurityException
	{
		this.checkPermission(new RuntimePermission("modifyThread"));
	}
	
	@Api
	public void checkConnect(String __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void checkDelete(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Checks that the virtual machine can exit with the given code.
	 *
	 * @param __code The exit code.
	 * @throws SecurityException If exit is not permitted.
	 * @since 2018/10/13
	 */
	@Api
	public void checkExit(int __code)
		throws SecurityException
	{
		this.checkPermission(new RuntimePermission("exitVM." + __code));
	}
	
	@Api
	public void checkListen(int __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Checks whether the given permission is permitted.
	 *
	 * @param __p The permission to check.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If permission is denied.
	 * @since 2018/09/18
	 */
	@Api
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
	@Api
	public void checkPropertyAccess(String __key)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ1j Request to check access to system property
		with an empty key.} */
		if (__key.isEmpty())
			throw new IllegalArgumentException("ZZ1j");
		
		// Forward
		this.checkPermission(new PropertyPermission(__key, "read"));
	}
	
	@Api
	public void checkRead(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void checkWrite(String __a)
	{
		throw Debugging.todo();
	}
}

