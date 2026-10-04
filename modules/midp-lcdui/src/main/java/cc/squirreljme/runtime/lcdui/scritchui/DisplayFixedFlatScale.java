// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

/**
 * Fixed flat display scale.
 *
 * @since 2024/03/21
 */
public class DisplayFixedFlatScale
	extends DisplayScale
{
	/** The height. */
	protected final int height;
	
	/** The width. */
	protected final int width;
	
	/**
	 * Initializes the fixed flat scale.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2024/03/21
	 */
	public DisplayFixedFlatScale(int __w, int __h)
	{
		this.width = __w;
		this.height = __h;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/12
	 */
	@Override
	public boolean requiresBuffer()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int screenX(int __x)
	{
		return __x;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int screenY(int __y)
	{
		return __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int textureH()
	{
		return this.height;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int textureMaxH()
	{
		return this.height;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int textureMaxW()
	{
		return this.width;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int textureW()
	{
		return this.width;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int textureX(int __x)
	{
		return __x;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/21
	 */
	@Override
	public int textureY(int __y)
	{
		return __y;
	}
}
