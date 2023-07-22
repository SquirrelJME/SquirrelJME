// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.security;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown when access to a system critical resource such as a file
 * or network stream is denied.
 *
 * @since 2018/09/18
 */
@Api
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
	 * @since 2018/09/18
	 */
	@Api
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
	@Api
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
	@Api
	public Permission getPermission()
	{
		return this._permission;
	}
}

