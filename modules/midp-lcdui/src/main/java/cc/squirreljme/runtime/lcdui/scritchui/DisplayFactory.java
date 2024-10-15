// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Display;

/**
 * Factory for creating {@link Display} instances.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface DisplayFactory
{
	/**
	 * Creates a display for the given screen.
	 *
	 * @param __scritch The scritch interface in use.
	 * @param __window The window the display manages.
	 * @param __screen The screen to create for.
	 * @return The resultant display.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	DisplayState create(ScritchInterface __scritch,
		ScritchWindowBracket __window, ScritchScreenBracket __screen)
		throws NullPointerException;
}
