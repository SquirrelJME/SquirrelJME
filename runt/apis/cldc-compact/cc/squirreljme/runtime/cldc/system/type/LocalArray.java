// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.type;

/**
 * This specifies that the given array is local to the system.
 *
 * @since 2018/03/18
 */
public interface LocalArray
	extends Array
{
	/**
	 * Returns the local access so that it is directly accessed.
	 *
	 * @return The local array.
	 * @since 2018/03/24
	 */
	public abstract Object localArray();
}

