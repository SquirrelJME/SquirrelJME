// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.game.GameCanvas;

/**
 * Specific flags for specific {@link Displayable}s, note that every
 * displayable does not share any flag however they will inherit flags from
 * parent classes.
 * 
 * This means that both {@link Canvas} and {@link GameCanvas} may have the
 * flag {@link #CANVAS_SUPPRESS_GAME_KEY}, however {@link Form} will not as
 * it is a completely different {@link Displayable}.
 * 
 * This is used with {@link DisplayableState#flags()} and
 * {@link DisplayableState#flags(int)}.
 *
 * @since 2026/08/09
 */
@SquirrelJMEVendorApi
public interface SpecificFlags
{
	/** {@link Canvas}: Suppress key events. */
	@SquirrelJMEVendorApi
	byte CANVAS_SUPPRESS_GAME_KEY =
		1;
}
