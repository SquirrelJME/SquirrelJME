// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.linkage.Linkage;

/**
 * This interface is used to provide an interface that the translation engine
 * can use to interact directly with the JIT translator for the current method.
 *
 * @since 2017/02/16
 */
public interface JITStateAccessor
{
	/**
	 * Returns the state of cached variables across the entire program.
	 *
	 * @return The cache states, or {@code null} if they are not yet available.
	 * @since 2017/02/16
	 */
	public abstract SnapshotCacheStates cacheStates();
	
	/**
	 * This returns the index of link index to another class, field, or method.
	 *
	 * @param __l The external linkage to get the link index for.
	 * @return The link index.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/02
	 */
	public abstract int link(Linkage __l)
		throws NullPointerException;
	
	/**
	 * Returns the offsets for stack slots.
	 *
	 * @return The stack slot offset table.
	 * @since 2017/03/11
	 */
	public abstract StackSlotOffsets stackSlotOffset();
}

