// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This describes class interfaces which have access flags associated with them
 * (such as private, protected, or public).
 *
 * @since 2016/05/12
 */
public interface CIAccessibleObject
{
	/**
	 * Returns flags which are appropriate for access checking.
	 *
	 * @return Flags which are capable of being access checked.
	 * @since 2016/05/12
	 */
	public abstract CIAccessibleFlags flags();
	
	/**
	 * Returns the class which contains this accessible object or {@code this}
	 * if this is a class.
	 *
	 * @return The containing class or {@code this} if this is a class.
	 * @since 2016/05/12
	 */
	public abstract CIClass outerClass();
}

