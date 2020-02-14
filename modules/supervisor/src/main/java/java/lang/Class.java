// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;

/**
 * This represents the Java accessible class object which describes this
 * class.
 *
 * @param <C> The class type.
 * @since 2019/05/26
 */
public final class Class<C>
{
	/**
	 * Returns the super class of this class.
	 *
	 * @return The super class of this class.
	 * @since 2019/05/26
	 */
	public final Class<?> getSuperclass()
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

