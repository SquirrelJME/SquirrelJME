// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * These are flags which are associated with accessible objects to determine
 * if they can be accessed from one location to another.
 *
 * @since 2016/05/12
 */
public interface AccessibleFlags
{
	/**
	 * Is the current accessible object package private?
	 *
	 * @return {@code true} if package private.
	 * @since 2016/05/12
	 */
	public abstract boolean isPackagePrivate();
	
	/**
	 * Is the current accessible object private?
	 *
	 * @return {@code true} if private.
	 * @since 2016/05/12
	 */
	public abstract boolean isPrivate();
	
	/**
	 * Is the current accessible object protected?
	 *
	 * @return {@code true} if protected.
	 * @since 2016/05/12
	 */
	public abstract boolean isProtected();
	
	/**
	 * Is the current accessible object public?
	 *
	 * @return {@code true} if public.
	 * @since 2016/05/12
	 */
	public abstract boolean isPublic();
}

