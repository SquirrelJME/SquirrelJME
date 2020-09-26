// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Capabilities for the hardware accelerated pencil graphics drawing.
 * 
 * This interface contains bit-fields.
 *
 * @since 2020/09/25
 */
public interface PencilCapabilities
{
	/**
	 * Minimum capabilities required by the implementation, this includes all
	 * of that state operations such as: transforms, clips, colors, styles,
	 * and otherwise.
	 */
	byte MINIMUM =
		1;
}
