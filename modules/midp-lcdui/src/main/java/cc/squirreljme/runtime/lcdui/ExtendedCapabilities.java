// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

/**
 * Represents extended capabilities of the display.
 *
 * @since 2019/05/05
 */
public interface ExtendedCapabilities
{
	/** Does this support pointer events? */
	public static final int SUPPORTS_POINTER_EVENTS =
		0x4000_0000;
}

