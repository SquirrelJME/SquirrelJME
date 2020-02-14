// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import java.util.Iterator;

/**
 * This represents a class which can be iterated through giving one or more
 * values.
 *
 * @param <T> The type this returns.
 * @since 2018/12/08
 */
public interface Iterable<T>
{
	/**
	 * Returns the iterator over the object.
	 *
	 * @return The object iterator.
	 * @since 2018/12/08
	 */
	public abstract Iterator<T> iterator();
}

