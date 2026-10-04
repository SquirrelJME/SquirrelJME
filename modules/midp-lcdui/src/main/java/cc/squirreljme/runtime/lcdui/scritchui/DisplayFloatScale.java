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
 * Floating point scaling for displays.
 *
 * @since 2024/03/09
 */
public class DisplayFloatScale
	extends DisplayScale
{
	/** The base scale to use. */
	protected final DisplayScale base;
	
	/** Source texture width. */
	private final int textureW;
	
	/** Source texture height. */
	private final int textureH;
	
	/** Scaled target width. */
	private final int scaledW;
	
	/** Scaled target height. */
	private final int scaledH;
	
	/** X multiplier. */
	private final float mulX;
	
	/** Y multiplier. */
	private final float mulY;
	
	/**
	 * Initializes the scaling information.
	 *
	 * @param __base The base scale to use.
	 * @param __scaledW The scaled width.
	 * @param __scaledH The scaled height.
	 * @throws IllegalArgumentException If the scale target is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/11
	 */
	public DisplayFloatScale(DisplayScale __base,
		int __scaledW, int __scaledH)
		throws IllegalArgumentException, NullPointerException
	{
		if (__base == null)
			throw new NullPointerException("NARG");
		
		this.base = __base;
		this.textureW = __base.textureW();
		this.textureH = __base.textureH();
		this.scaledW = __scaledW;
		this.scaledH = __scaledH;
		this.mulX = ((float)__scaledW / (float)this.textureW);
		this.mulY = ((float)__scaledH / (float)this.textureH);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/12
	 */
	@Override
	public boolean requiresBuffer()
	{
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenX(int __x)
	{
		return (int)(__x * this.mulX);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenY(int __y)
	{
		return (int)(__y * this.mulY);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int textureH()
	{
		return this.textureH;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/11
	 */
	@Override
	public int textureMaxH()
	{
		return this.textureH;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/11
	 */
	@Override
	public int textureMaxW()
	{
		return this.textureW;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int textureW()
	{
		return this.textureW;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureX(int __x)
	{
		return (int)(__x / this.mulX);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureY(int __y)
	{
		return (int)(__y / this.mulY);
	}
}
