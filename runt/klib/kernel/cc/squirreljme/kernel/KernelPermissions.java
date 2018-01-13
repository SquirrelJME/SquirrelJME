// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

/**
 * This class is used to manage permissions which are used to determine what
 * a task can or cannot do.
 *
 * This class is not immutable but it is thread safe. It is not immutable so
 * that when a process is performing an action it can be granted potentially
 * to allow the operation to complete.
 *
 * @since 2018/01/02
 */
public final class KernelPermissions
{
	/**
	 * Checks if the specified permission is permitted, if it is not then a
	 * {@link SecurityException} is thrown.
	 *
	 * @param __p The permission to check.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/09
	 */
	public final void checkPermission(KernelPermission __p)
		throws NullPointerException, SecurityException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.checkPermission(__p.className(), __p.name(), __p.action());
	}
	
	/**
	 * Checks if the specified permission is permitted, if it is not then a
	 * {@link SecurityException} is thrown.
	 *
	 * @param __cl The class type of the permission.
	 * @param __n The name of the permission.
	 * @param __a The actions in the permission.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the permissions is not permitted.
	 * @since 2018/01/09
	 */
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

