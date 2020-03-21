// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This is used to wrap the reference counter.
 *
 * @since 2020/03/13
 */
public final class ReferenceCounter
{
	/**
	 * The current count.
	 *
	 * @deprecated This will be transitioned to somewhere in memory.
	 */
	@Deprecated
	private int _count =
		1;
	
	/**
	 * Increases the count and returns the new value.
	 *
	 * @return The new value.
	 * @since 2020/03/13
	 */
	public final int up()
	{
		return ++this._count;
	}
}
