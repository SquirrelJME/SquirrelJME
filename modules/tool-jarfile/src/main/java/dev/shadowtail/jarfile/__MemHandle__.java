// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import cc.squirreljme.jvm.summercoat.constants.BootstrapConstants;

/**
 * Represents a memory handle.
 *
 * @since 2020/12/16
 */
final class __MemHandle__
{
	/** The memory handle ID. */
	public final int id;
	
	/**
	 * Initializes the base memory handle.
	 * 
	 * @param __id The memory handle ID.
	 * @throws IllegalArgumentException If the memory handle does not have the
	 * correct security bits specified.
	 * @since 2020/12/16
	 */
	__MemHandle__(int __id)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BC01 Invalid memory handle security bit IDs.
		// (The handle)}
		if ((__id & BootstrapConstants.HANDLE_SECURITY_MASK) !=
			BootstrapConstants.HANDLE_SECURITY_BITS)
			throw new IllegalArgumentException(
				"BC01 0b" + Integer.toString(__id, 2));
		
		this.id = __id;
	}
}
