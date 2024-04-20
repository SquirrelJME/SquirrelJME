// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

/**
 * This has a native object pointer.
 *
 * @since 2024/04/20
 */
public interface DylibHasObjectPointer
{
	/**
	 * Returns the object pointer.
	 *
	 * @return The object pointer.
	 * @since 2024/04/20
	 */
	long objectPointer();
}
