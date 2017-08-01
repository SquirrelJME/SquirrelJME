// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import net.multiphasicapps.squirreljme.lcdui.NativeCanvas;
import net.multiphasicapps.squirreljme.lcdui.NativeDisplay;

/**
 * This is a canvas which provides support for being used on Swing to provide
 * an interface for rendering graphics.
 *
 * @since 2017/05/24
 */
public class SwingCanvas
	extends NativeCanvas
{
	/**
	 * Initializes the Swing canvas.
	 *
	 * @param __ref The back reference to the displayable.
	 * @since 2017/05/24
	 */
	public SwingCanvas(Reference<Displayable> __ref)
	{
		super(__ref);
	}
}

