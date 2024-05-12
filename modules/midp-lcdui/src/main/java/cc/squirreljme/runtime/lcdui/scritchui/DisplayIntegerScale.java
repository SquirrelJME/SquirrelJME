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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Integer scaling for displays.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public class DisplayIntegerScale
	extends DisplayScale
{
	/** The base screen. */
	protected final ScritchScreenBracket screen;
	
	/** The base window. */
	protected final ScritchWindowBracket window;
	
	/** The scritch interface to use. */
	protected final ScritchInterface scritch;
	
	/** Scaled target width. */
	private final int scaledW;
	
	/** Scaled target height. */
	private final int scaledH;
	
	/**
	 * Initializes the scaling information.
	 *
	 * @param __scritch The ScritchUI interface to use.
	 * @param __screen The screen to access.
	 * @param __window The window to access.
	 * @param __scaledW The scaled width.
	 * @param __scaledH The scaled height.
	 * @throws IllegalArgumentException If the scale target is invalid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/11
	 */
	public DisplayIntegerScale(ScritchInterface __scritch,
		ScritchScreenBracket __screen,
		ScritchWindowBracket __window, int __scaledW, int __scaledH)
		throws IllegalArgumentException, NullPointerException
	{
		if (__scritch == null || __screen == null || __window == null)
			throw new NullPointerException("NARG");
		
		this.scritch = __scritch;
		this.screen = __screen;
		this.window = __window;
		this.scaledW = __scaledW;
		this.scaledH = __scaledH;
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
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenY(int __y)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int textureH()
	{
		return this.scaledH;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/11
	 */
	@Override
	public int textureMaxH()
	{
		return this.scaledH;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/11
	 */
	@Override
	public int textureMaxW()
	{
		return this.scaledW;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int textureW()
	{
		return this.scaledW;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureX(int __x)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureY(int __y)
	{
		throw Debugging.todo();
	}
}
