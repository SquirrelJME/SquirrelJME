// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

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
	boolean isPackagePrivate();
	
	/**
	 * Is the current accessible object private?
	 *
	 * @return {@code true} if private.
	 * @since 2016/05/12
	 */
	boolean isPrivate();
	
	/**
	 * Is the current accessible object protected?
	 *
	 * @return {@code true} if protected.
	 * @since 2016/05/12
	 */
	boolean isProtected();
	
	/**
	 * Is the current accessible object public?
	 *
	 * @return {@code true} if public.
	 * @since 2016/05/12
	 */
	boolean isPublic();
}

