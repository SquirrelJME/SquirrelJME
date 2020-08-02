// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import javax.microedition.lcdui.Display;
import lcdui.BaseDisplay;

/**
 * Base tests for canvases.
 *
 * @since 2020/07/27
 */
public abstract class BaseCanvas
	extends BaseDisplay
{
	/**
	 * Performs the canvas test.
	 * 
	 * @param __display The display being run on.
	 * @param __platform The canvas platform used.
	 * @throws Throwable On any exception.
	 * @since 2020/07/27
	 */
	public abstract void test(Display __display, CanvasPlatform __platform)
		throws Throwable;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/27
	 */
	@Override
	public final void test(Display __display)
		throws Throwable
	{
		CanvasPlatform platform = new CanvasPlatform();
		__display.setCurrent(platform);
		
		try
		{
			this.test(__display, platform);
		}
		finally
		{
			if (__display.getCurrent() == platform)
				__display.removeCurrent();
		}
	}
}
