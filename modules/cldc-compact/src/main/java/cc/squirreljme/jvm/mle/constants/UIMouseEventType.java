// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Mouse event for {@link UIFormCallback#eventMouse(cc.squirreljme.jvm.mle.brackets.UIDrawableBracket, int, int, int, int, int)}.
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
