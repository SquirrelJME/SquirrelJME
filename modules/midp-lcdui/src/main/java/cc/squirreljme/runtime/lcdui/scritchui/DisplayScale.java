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

/**
 * Interface for display scaling.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public abstract class DisplayScale
{
	/**
	 * Does this display scale require a buffer?
	 *
	 * @return If a buffer is required for scaling.
	 * @since 2024/05/12
	 */
	@SquirrelJMEVendorApi
	public abstract boolean requiresBuffer();
	
	/**
	 * Projects a texture coordinate to a screen coordinate.
	 *
	 * @param __x The input texture coordinate.
	 * @return The output screen coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int screenX(int __x);
	
	/**
	 * Projects a texture coordinate to a screen coordinate.
	 *
	 * @param __y The input texture coordinate.
	 * @return The output screen coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int screenY(int __y);
	
	/**
	 * Returns the current texture height.
	 *
	 * @return The current texture height.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	public abstract int textureH();
	
	/**
	 * Returns the max height of the scaled target texture.
	 *
	 * @return The target texture height.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	public abstract int textureMaxH();
	
	/**
	 * Returns the max width of the scaled target texture.
	 *
	 * @return The target texture width.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	public abstract int textureMaxW();
	
	/**
	 * Returns the current texture width.
	 *
	 * @return The current texture width.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	public abstract int textureW();
	
	/**
	 * Projects a screen coordinate to a texture coordinate.
	 *
	 * @param __x The input screen coordinate.
	 * @return The output texture coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int textureX(int __x);
	
	/**
	 * Projects a screen coordinate to a texture coordinate.
	 *
	 * @param __y The input screen coordinate.
	 * @return The output texture coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public abstract int textureY(int __y);
	
	/**
	 * Returns the display scale that currently should be used.
	 *
	 * @param __scritch The Scritch API used.
	 * @param __screen The screen to draw on.
	 * @param __window The window for the display.
	 * @return The resultant scale.
	 * @since 2024/03/21
	 */
	public static DisplayScale currentScale(ScritchInterface __scritch,
		ScritchScreenBracket __screen, ScritchWindowBracket __window)
		throws NullPointerException
	{
		return new DisplayFixedFlatScale(240, 320);
	}
}
