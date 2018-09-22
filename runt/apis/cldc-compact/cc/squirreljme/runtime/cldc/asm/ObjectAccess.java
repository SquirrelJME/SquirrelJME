// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * This contains accessors for object information.
 *
 * @since 2018/09/22
 */
public final class ObjectAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/09/22
	 */
	private ObjectAccess()
	{
	}
	
	/**
	 * Returns the class object for the given object.
	 *
	 * @param __v The object to get the class of.
	 * @return The class of the given object, or {@code null} if it has no
	 * class.
	 * @since 2018/09/22
	 */
	public static final native Class<?> classOf(Object __v);
}

