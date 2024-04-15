// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

/**
 * Represents a pointer somewhere in memory.
 *
 * @since 2023/12/05
 */
public interface Pointer
{
	/**
	 * The address where the pointer is.
	 *
	 * @return Returns the address pointer.
	 * @since 2023/12/05
	 */
	long pointerAddress();
}
