// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Interface for display scaling.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface DisplayScale
{
	/**
	 * Projects a texture coordinate to a screen coordinate.
	 *
	 * @param __x The input texture coordinate.
	 * @return The output screen coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	int screenX(int __x);
	
	/**
	 * Projects a texture coordinate to a screen coordinate.
	 *
	 * @param __y The input texture coordinate.
	 * @return The output screen coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	int screenY(int __y);
	
	/**
	 * Returns the current texture height.
	 *
	 * @return The current texture height.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	int textureH();
	
	/**
	 * Returns the max height of the scaled target texture.
	 *
	 * @return The target texture height.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	int textureMaxH();
	
	/**
	 * Returns the max width of the scaled target texture.
	 *
	 * @return The target texture width.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	int textureMaxW();
	
	/**
	 * Returns the current texture width.
	 *
	 * @return The current texture width.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	int textureW();
	
	/**
	 * Projects a screen coordinate to a texture coordinate.
	 *
	 * @param __x The input screen coordinate.
	 * @return The output texture coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	int textureX(int __x);
	
	/**
	 * Projects a screen coordinate to a texture coordinate.
	 *
	 * @param __y The input screen coordinate.
	 * @return The output texture coordinate.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	int textureY(int __y);
}
