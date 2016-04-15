// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import net.multiphasicapps.descriptors.BinaryNameSymbol;

/**
 * This is a class or member of a class which has access modifier flags
 * associated with it.
 *
 * @since 2016/04/09
 */
public interface JVMAccessibleObject
{
	/**
	 * Is this object package private?
	 *
	 * @return {@code true} if it is package private.
	 * @sicne 2016/04/16
	 */
	public abstract boolean isPackagePrivate();
	
	/**
	 * Is this object private?
	 *
	 * @return {@code true} if it is private.
	 * @sicne 2016/04/16
	 */
	public abstract boolean isPrivate();
	
	/**
	 * Is this object protected?
	 *
	 * @return {@code true} if it is protected.
	 * @sicne 2016/04/16
	 */
	public abstract boolean isProtected();
	
	/**
	 * Is this object public?
	 *
	 * @return {@code true} if it is public.
	 * @since 2016/04/15
	 */
	public abstract boolean isPublic();
}

