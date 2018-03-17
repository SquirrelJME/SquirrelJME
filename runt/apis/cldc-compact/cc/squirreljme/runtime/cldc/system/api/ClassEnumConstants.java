// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Interface for {@link SystemFunction#CLASS_ENUM_CONSTANTS}.
 *
 * @since 2018/03/17
 */
public interface ClassEnumConstants
{
	/**
	 * Returns the enumeration constants for the given class.
	 *
	 * @param <E> The type of the class.
	 * @param __cl The class to get the constants for.
	 * @return The constants for the class or {@code null} if it is not an
	 * enumeration.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public abstract <E> E[] classEnumConstants(Class<E> __cl)
		throws NullPointerException;
}

