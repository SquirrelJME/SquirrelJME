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
 * A list that is a number of class handles.
 *
 * @since 2020/12/21
 */
public class ClassInfoListHandle
	extends ListValueHandle
{
	/**
	 * Initializes the base class list handle.
	 *
	 * @param __id The memory handle ID.
	 * @param __memActions Memory actions used.
	 * @param __count The number of handles to reference.
	 * @throws IllegalArgumentException If the memory handle does not have the
	 * correct security bits specified or if the count is negative.
	 * @since 2020/12/21
	 */
	ClassInfoListHandle(int __id, MemActions __memActions, int __count)
		throws IllegalArgumentException
	{
		super(__id, __memActions, __count);
	}
}
