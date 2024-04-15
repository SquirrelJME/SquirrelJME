// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is used for access control on resources that may be available at
 * run-time.
 * 
 * SquirrelJME has two levels of access control, one that is defined at the
 * VM level which is checked first and then there is the application level
 * policy which may be set with system properties. If a permission is denied
 * at the VM level then it will be denied even if the application level
 * property permits this, this is to prevent policies being broken by the
 * application.
 * 
 * The first level of control is using the
 * {@link RuntimeShelf#vmDescription(int)} with
 * {@link VMDescriptionType#VM_SECURITY_POLICY}. If a permission is ultimately
 * denied by this level of the policy then the permission check will fail.
 * 
 * The other level of control is with the system property
 * {@code cc.squirreljme.policy} which is in the same format.
 * 
 * Compared to Java SE, SquirrelJME uses a more simplified security policy
 * format that while more limited is simpler to write.
 *
 * @since 2018/09/18
 */
@Api
public final class AccessController
{
	/**
	 * {@squirreljme.property cc.squirreljme.policy=policies
	 * This system property defines the application-level policies that are
	 * in effect for the current program, these are not at a virtual machine
	 * level.}
	 */
	private static final String _POLICY_PROPERTY =
		"cc.squirreljme.policy";
	
	/**
	 * Checks the specified permission.
	 *
	 * @param __p The permission to check.
	 * @throws AccessControlException If access is denied or the permission
	 * is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/18
	 */
	@Api
	public static void checkPermission(Permission __p)
		throws AccessControlException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		Debugging.todoNote("Check permission: %s", __p);
	}
}

