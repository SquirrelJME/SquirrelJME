// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

/**
 * This is a handle which represents a list of properties.
 *
 * @since 2020/12/20
 */
public abstract class PropertyListHandle
	extends MemHandle
{
	/**
	 * Initializes the base property list handle.
	 *
	 * @param __id The memory handle ID.
	 * @throws IllegalArgumentException If the memory handle does not have the
	 * correct security bits specified.
	 * @since 2020/12/20
	 */
	PropertyListHandle(int __id)
		throws IllegalArgumentException
	{
		super(__id);
	}
}
