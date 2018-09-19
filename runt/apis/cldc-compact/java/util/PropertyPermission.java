// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import java.security.BasicPermission;
import java.security.Permission;
import java.security.PermissionCollection;

/**
 * This permission is used to specify access to a system property.
 *
 * Property names come in two formats: A fully specified string which matches
 * the key, or if it ends in asterisk {@code '*'} it will be a wildcard match
 * at the base string. Note that only the last character is considered if this
 * is a wildcard match.
 *
 * The following permissions are available:
 * {@code "read"} -- Read access to the property.
 * {@code "write"} -- Write access to the property.
 *
 * Multiple permissions may be specified, separated by comma.
 *
 * @since 2018/09/18
 */
public final class PropertyPermission
	extends BasicPermission
{
	/** The key to check or the prefix if a wildcard. */
	private final String _key;
	
	/** If this is a wildcard permission. */
	private final boolean _iswildcard;
	
	/** Is this read permission? */
	private final boolean _isread;
	
	/** Is this write permission? */
	private final boolean _iswrite;
	
	/**
	 * Initializes the permission.
	 *
	 * @param __key The key to check.
	 * @param __act The action on the key.
	 * @throws IllegalArgumentException If the action is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/18
	 */
	public PropertyPermission(String __key, String __act)
		throws IllegalArgumentException, NullPointerException
	{
		super(__key, __act);
		
		if (__key == null || __act == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ13 Property permission key cannot be
		// negative.}
		if (__key.isEmpty())
			throw new IllegalArgumentException("ZZ13");
		
		// Setup key first
		this._key = __key;
		this._iswildcard = (__key.charAt(__key.length() - 1) == '*');
		
		// Convert action to lowercase first
		__act = __act.toLowerCase();
		
		// This field is split with commas and may contain read or write
		boolean r = false,
			w = false;
		for (int i = 0, n = __act.length(); i < n;)
		{
			// Find the next comma, if there is none use the end of string
			int dx = __act.indexOf(',', i);
			if (dx < 0)
				dx = n;
			
			// Determine which action to take
			String sub;
			switch ((sub = __act.substring(i, dx)))
			{
				case "read":
					r = true;
					break;
				
				case "write":
					w = true;
					break;
				
					// {@squirreljme.error ZZ14 The specified action is not
					// valid for property permissions. (The action; The input
					// actions)}
				default:
					throw new IllegalArgumentException(
						String.format("ZZ14 %s %s", sub, __act));
			}
			
			// Remember to skip the comma, if there is one
			i = dx + 1;
		}
		
		// Read or writing?
		this._isread = r;
		this._iswrite = w;
	}
	
	@Override
	public boolean equals(Object __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public String getActions()
	{
		throw new todo.TODO();
	}
	
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
	
	@Override
	public boolean implies(Permission __a)
	{
		throw new todo.TODO();
	}
	
	@Override
	public PermissionCollection newPermissionCollection()
	{
		throw new todo.TODO();
	}
}

