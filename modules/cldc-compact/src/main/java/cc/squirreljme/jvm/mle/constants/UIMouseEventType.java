// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;

/**
 * Mouse event for {@link UIFormCallback#eventMouse(UIFormBracket,
 * UIItemBracket, int, int, int, int, int)}.
 *
 * @since 2020/07/19
 */
public interface UIMouseEventType
{
	/** Mouse down. */
	byte MOUSE_DOWN =
		0;
	
	/** Mouse up. */
	byte MOUSE_UP =
		1;
	
	/** Mouse dragged. */
	byte MOUSE_DRAGGED =
		2;
	
	/** Mouse entered. */
	byte MOUSE_ENTERED =
		3;
	
	/** Mouse exited. */
	byte MOUSE_EXITED =
		4;
	
	/** The number of events. */
	byte NUM_MOUSE_EVENTS =
		5;
}
