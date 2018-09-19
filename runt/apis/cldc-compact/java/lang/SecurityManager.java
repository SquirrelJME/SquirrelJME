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
	
	public void checkPropertyAccess(String __a)
	{
		throw new todo.TODO();
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

