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

/**
 * Interface for {@link SystemFunction#CLASS_ENUM_COUNT}.
 *
 * @since 2018/03/21
 */
public interface ClassEnumCount
{
	/**
	 * Returns the number of elements used in the given enumeration.
	 *
	 * @param <E> The type of the class.
	 * @param __cl The class to get the constant for.
	 * @param __i The index of the constant to get.
	 * @return The enumeration constant.
	 * @throws IllegalArgumentException If the enumeration is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/21
	 */
	public abstract int classEnumCount(Class<? extends Enum> __cl)
		throws IllegalArgumentException, NullPointerException;
}

