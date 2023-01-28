// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Represents the mouse button that was used.
 *
 * @since 2020/07/19
 */
@Exported
public interface UIMouseButton
{
	/** Primary mouse button. */
	@Exported
	byte PRIMARY_BUTTON =
		0;
	
	/** Secondary mouse button. */
	@Exported
	byte SECONDARY_BUTTON =
		1;
	
	/** The number of buttons. */
	@Exported
	byte NUM_BUTTONS =
		2;
}
