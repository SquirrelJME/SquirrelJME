// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayAccess;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This class forwards graphics operations to the host native display API
 * which provides a single instance of accelerated graphics.
 *
 * @since 2018/11/19
 */
public final class AcceleratedGraphics
	extends SerializedGraphics
{
	/** The display to use for drawing the graphics. */
	protected final int display;
	
	/**
	 * Initializes for the given display.
	 *
	 * @param __did The display ID.
	 * @since 2018/11/19
	 */
	AcceleratedGraphics(int __did)
	{
		this.display = __did;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/19
	 */
	@Override
	public final Object serialize(GraphicsFunction __func, Object... __args)
	{
		// Forward serialized arguments to the destination host
		return NativeDisplayAccess.accelGfxFunc(this.display,
			__func.ordinal(), __args);
	}
	
	/**
	 * Returns the instance for the given display ID.
	 *
	 * @param __did The display ID.
	 * @return The accelerated graphics instance.
	 * @throws UnsupportedOperationException If accelerated graphics are not
	 * supported.
	 * @since 2018/11/19
	 */
	public static final AcceleratedGraphics instance(int __did)
		throws UnsupportedOperationException
	{
		// {@squirreljme.error EB2c Accelerated graphics operations are not
		// supported for this display. (The display ID)}
		if (!NativeDisplayAccess.accelGfx(__did))
			throw new UnsupportedOperationException("EB2c " + __did);
		
		// Effectively has "new" state
		return new AcceleratedGraphics(__did);
	}
}

