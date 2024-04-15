// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import javax.microedition.lcdui.Image;

/**
 * Represents an image that is accessible for graphics operations.
 * 
 * @since 2022/01/26
 */
public abstract class AccessibleImage
{
	/**
	 * Returns the offset into the buffer.
	 * 
	 * @return The offset into the buffer.
	 * @since 2022/01/26
	 */
	public abstract int squirreljmeDirectOffset();
	
	/**
	 * Returns the direct buffer to the image.
	 *
	 * @return The direct RGB buffer for the image.
	 * @since 2022/01/26
	 */
	public abstract int[] squirreljmeDirectRGBInt();
	
	/**
	 * Returns the scanline width of the image.
	 * 
	 * @return The scanline width of the image.
	 * @since 2022/01/26
	 */
	public abstract int squirreljmeDirectScanLen();
	
	/**
	 * Returns if this {@link Image} is directly accessible.
	 *
	 * @return Returns {@code true} if this is directly accessible.
	 * @since 2022/01/26
	 */
	public abstract boolean squirreljmeIsDirect();
}
