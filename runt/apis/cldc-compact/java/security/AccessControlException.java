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

/**
 * This is thrown when access to a system critical resource such as a file
 * or network stream is denied.
 *
 * @since 2018/09/18
 */
public class AccessControlException
	extends SecurityException
{
	/** The permission which was set, is optional. */
	private final Permission _permission;
	
	/**
	 * Initializes the exception with the given message with no permission or
	 * cause.
	 *
	 * @param __s The message.
	 * @param __p The permission.
	 * @since 2018/09/18
	 */
	public AccessControlException(String __s)
	{
		super(__s);
		
		this._permission = null;
	}
	
	/**
	 * Initializes the exception with the given message and permission, no
	 * cause is used.
	 *
	 * @param __s The message.
	 * @param __p The permission.
	 * @since 2018/09/18
	 */
	public AccessControlException(String __s, Permission __p)
	{
		super(__s);
		
		this._permission = __p;
	}
	
	/**
	 * Returns the permission that was specified, if one was.
	 *
	 * @return The specified permission or {@code null} if there was none.
	 * @since 2018/09/18
	 */
	public Permission getPermission()
	{
		return this._permission;
	}
}

