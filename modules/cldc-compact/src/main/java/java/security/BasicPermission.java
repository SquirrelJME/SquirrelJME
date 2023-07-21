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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class represents a basic permission and is the root of most
 * permissions.
 * 
 * Wildcards for permissions have two means of handling:
 * 
 * There are pure wildcards which consist of a single {@code '*'} character,
 * this means that all permissions are implied by it.
 * 
 * 
 * 
 * There may be a wildcard in the name (ending in an asterisk {@code '*'}),
 * in which case the start of the permission string is compared while the
 * other cases are all matches. Note that only a single trailing asterisk is
 * valid and that only the start of the string is checked. Wildcards mean
 * that {@code foo.*} implies {@code foo.bar}.
 * 
 * @since 2020/07/09
 */
@Api
public abstract class BasicPermission
	extends Permission
{
	/** The index of the wildcard character, will be {@code < 0} if not. */
	private final int _wildCardDx;
	
	/**
	 * Initializes the basic permission.
	 *
	 * @param __name The name of the permission.
	 * @throws IllegalArgumentException If name is empty.
	 * @throws NullPointerException If no name was specified.
	 * @since 2018/09/18
	 */
	@Api
	public BasicPermission(String __name)
		throws IllegalArgumentException, NullPointerException
	{
		this(__name, null);
	}
	
	/**
	 * Initializes the basic permission.
	 *
	 * @param __name The name of the permission.
	 * @param __act The action to use, this is ignored for basic permissions.
	 * @throws IllegalArgumentException If name is empty.
	 * @throws NullPointerException If no name was specified.
	 * @since 2018/09/18
	 */
	@Api
	public BasicPermission(String __name,
		@SuppressWarnings("unused") String __act)
		throws IllegalArgumentException, NullPointerException
	{
		super(__name);
		
		if (__name == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ2b The name for basic permissions cannot
		be empty.} */
		if (__name.equals(""))
			throw new IllegalArgumentException("ZZ0z");
		
		// Determine the index where the wildcard is, but it is only valid
		// at the end of a string, otherwise it is just treated as a normal
		// character
		int nameLen = __name.length();
		int wildCardDx = __name.lastIndexOf('*');
		
		// Global wildcard
		if (nameLen == 1 && wildCardDx == 0)
			this._wildCardDx = 0;
		
		// Has wildcard at the end, and is at least two chars wide, and the
		// wildcard is at the end
		else if (nameLen > 1 && wildCardDx > 1 && wildCardDx == nameLen - 1)
		{
			// Wildcards are only valid if they are preceded by a dot
			char beforeStar = __name.charAt(wildCardDx - 1);
			if (beforeStar == '.')
				this._wildCardDx = wildCardDx;
			
			// Otherwise not a valid wildcard
			else
				this._wildCardDx = -1;
		}
		
		// Not valid wildcard
		else
			this._wildCardDx = -1;
	}
	
	/**
	 * The {@link BasicPermission} implementation checks if {@code __o} is
	 * the same exact class and that the name matches as well.
	 * 
	 * {@inheritDoc}
	 * @since 2020/07/09
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Short circuit match?
		if (this == __o)
			return true;
		
		if (__o == null)
			return false;
		
		// This is only valid
		if (this.getClass() != __o.getClass())
			return false;
		
		return this.getName().equals(((BasicPermission)__o).getName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/08
	 */
	@Override
	public String getActions()
	{
		// BasicPermissions do not have any actions so this will always return
		// a blank string as they are ignored
		return "";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/09
	 */
	@Override
	public int hashCode()
	{
		// This only uses the name as actions are ignored
		return this.getName().hashCode();
	}
	
	/**
	 * Checks if the specified permission is implied by this one. It will be
	 * implied if the class type is the same and there is a wildcard match
	 * if they are being used (i.e. {@code foo.*} implies {@code foo.bar}).
	 * 
	 * {@inheritDoc}
	 * @since 2020/07/09
	 */
	@Override
	public boolean implies(Permission __o)
	{
		// Null permissions are never implied, or is a different class
		if (__o == null || this.getClass() != __o.getClass())
			return false;
		
		// If self, this is always implied
		if (this == __o)
			return true;
		
		// If the string is a complete match then we can just stop here
		String self = this.getName();
		String other = __o.getName();
		if (self.equals(other))
			return true;
		
		// Matching against a wildcard?
		int selfWild = this._wildCardDx;
		int otherWild = ((BasicPermission)__o)._wildCardDx;
		if (selfWild >= 0)
			return self.regionMatches(0, other, 0, selfWild);
		
		// Does not imply
		return false;
	}
	
	@Override
	public PermissionCollection newPermissionCollection()
	{
		throw Debugging.todo();
	}
}

