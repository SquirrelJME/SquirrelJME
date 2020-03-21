// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.memory;

/**
 * This is any memory which can be sized.
 *
 * @since 2020/03/14
 */
public interface SizeableMemory
{
	/** Unknown memory size. */
	int UNKNOWN_SIZE =
		Integer.MIN_VALUE;
	
	/**
	 * Returns the size of this memory region.
	 *
	 * @return The region size or {@link #UNKNOWN_SIZE} if not known/undefined.
	 * @since 2020/03/14
	 */
	int size();
}
