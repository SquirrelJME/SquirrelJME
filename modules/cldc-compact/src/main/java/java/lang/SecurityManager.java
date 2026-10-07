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
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.annotation.KeepAbsolutelyEverything;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.security.AccessController;
import java.security.Permission;
import java.util.PropertyPermission;

/**
 * This is Java's Security Manager which is a software userspace access control
 * and permission checking system, and has since been permanently disabled in
 * Java SE 24. This system is only as secure as it's weakest link and is not
 * to be relied upon for any meaningful security beyond only the most
 * well-behaved, well-written, and securely-written applications. Malicious
 * or insecurely written programs will not be phased by this class.
 * 
 * In SquirrelJME, this is only here for compatibility purposes with legacy
 * applications. This should never be used for real security applications. If
 * you desire as such, please do investigate virtual machines, secure
 * computing, signature verification, and encrypted memory as examples. Apart
 * from the minimum required for legacy applications to operate, this will
 * remain a minimal application.
 * 
 * Access is checked by {@link AccessController#checkPermission(Permission)}.
 * 
 * @since 2020/07/02
 */
@Api
@ApiDefinedDeprecated
@KeepAbsolutelyEverything("Lightweight 'Security' permission system.")
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
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
	public void checkAccess(Thread __t)
		throws SecurityException
	{
		this.checkPermission(new RuntimePermission("modifyThread"));
	}
	
	@Api
	@ApiDefinedDeprecated
	public void checkConnect(String __a, int __b)
	{
		throw Debugging.todo();
	}
	
	@Api
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
	public void checkExit(int __code)
		throws SecurityException
	{
		this.checkPermission(new RuntimePermission("exitVM." + __code));
	}
	
	@Api
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
	public void checkRead(String __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	@ApiDefinedDeprecated
	public void checkWrite(String __a)
	{
		throw Debugging.todo();
	}
}

