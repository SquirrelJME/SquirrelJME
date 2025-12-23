// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.fe.midp;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * Canvas that is used in the exploration interface.
 *
 * @since 2025/12/21
 */
public class ExploreCanvas
	extends Canvas
{
	/**
	 * Initializes the exploration canvas.
	 *
	 * @since 2025/12/23
	 */
	public ExploreCanvas()
	{
		// Make this full screen
		this.setFullScreenMode(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/21
	 */
	@Override
	protected void paint(Graphics __g)
	{
		//throw Debugging.todo();
	}
}
