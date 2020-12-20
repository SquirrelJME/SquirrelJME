// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class contains the state of memory handles within the boot system.
 *
 * @since 2020/12/16
 */
public final class MemHandles
{
	/**
	 * Allocates a pool handle.
	 * 
	 * @param __sz The number of entries to place in.
	 * @return The allocated handle.
	 * @throws IllegalArgumentException If size is zero or negative.
	 * @since 2020/12/29
	 */
	public PoolHandle allocPool(int __sz)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
}
