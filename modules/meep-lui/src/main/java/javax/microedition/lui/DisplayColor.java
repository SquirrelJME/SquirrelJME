// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lui;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This represents a color to be used for text colors, background colors, and
 * lighting colors.
 *
 * @since 2016/08/30
 */
@Api
public class DisplayColor
{
	/** Color components. */
	private final int _rgb;
	
	/**
	 * Initializes the color using the given components, the components must
	 * be in the range of 0 through 255.
	 *
	 * @param __r Red color component.
	 * @param __g Green color component.
	 * @param __b Blue color component.
	 * @throws IllegalArgumentException If any component is outside of the
	 * range if {@code [0, 255]}.
	 * @since 2016/08/30
	 */
	@Api
	public DisplayColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error DA01 Initialization of color values out of
		range, they must be in the range of 0-255. (Red; Green; Blue)} */
		if (__r < 0 || __r > 255 || __g < 0 || __g > 255 || __b < 0 ||
			__b > 255)
			throw new IllegalArgumentException(String.format("DA01 %d %d %d",
				__r, __g, __b));
		
		// Set
		this._rgb = (__r << 16) | (__g << 8) | __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof DisplayColor))
			return false;
		
		// Cast and check
		DisplayColor o = (DisplayColor)__o;
		return this.getRedComponent() == o.getRedComponent() && this
			.getGreenComponent() == o.getGreenComponent() && this
			.getBlueComponent() == o.getBlueComponent();
	}
	
	/**
	 * Returns the blue component.
	 *
	 * @return The blue component.
	 * @since 2016/08/30
	 */
	@Api
	public int getBlueComponent()
	{
		return (this._rgb & 0xFF);
	}
	
	/**
	 * Returns the green component.
	 *
	 * @return The green component.
	 * @since 2016/08/30
	 */
	@Api
	public int getGreenComponent()
	{
		return ((this._rgb >>> 8) & 0xFF);
	}
	
	/**
	 * Returns the red component.
	 *
	 * @return The red component.
	 * @since 2016/08/30
	 */
	@Api
	public int getRedComponent()
	{
		return ((this._rgb >>> 16) & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public int hashCode()
	{
		return this._rgb;
	}
}

