// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Mouse event for {@link UIFormCallback#eventMouse(cc.squirreljme.jvm.mle.brackets.UIDrawableBracket, int, int, int, int, int)}.
 *
 * @since 2020/07/19
 */
@Exported
public interface UIMouseEventType
{
	/** Mouse down. */
	@Exported
	byte MOUSE_DOWN =
		0;
	
	/** Mouse up. */
	@Exported
	byte MOUSE_UP =
		1;
	
	/** Mouse dragged. */
	@Exported
	byte MOUSE_DRAGGED =
		2;
	
	/** Mouse entered. */
	@Exported
	byte MOUSE_ENTERED =
		3;
	
	/** Mouse exited. */
	@Exported
	byte MOUSE_EXITED =
		4;
	
	/** The number of events. */
	@Exported
	byte NUM_MOUSE_EVENTS =
		5;
}
