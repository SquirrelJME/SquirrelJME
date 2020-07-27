// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * A platform for testing canvases.
 *
 * @since 2020/07/26
 */
public class CanvasPlatform
	extends Canvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	protected void paint(Graphics __g)
	{
		throw Debugging.todo();
	}
}
