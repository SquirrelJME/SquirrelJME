// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

/**
 * A class library which represents an overlay.
 *
 * @since 2024/03/05
 */
public interface OverlayVMClassLibrary
	extends VMClassLibrary
{
	
	/**
	 * Returns the original base library.
	 *
	 * @return The original library.
	 * @since 2023/12/03
	 */
	VMClassLibrary originalLibrary();
}
