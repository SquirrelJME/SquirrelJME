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
 * No screen scaling.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public class DisplayIdentityScale
	extends DisplayScale
{
	/** The base screen. */
	protected final ScritchScreenBracket screen;
	
	/** The base window. */
	protected final ScritchWindowBracket window;
	
	/** The scritch interface to use. */
	protected final ScritchInterface scritch;
	
	/**
	 * Initializes the scaling information.
	 *
	 * @param __scritch The ScritchUI interface to use.
	 * @param __screen The screen to access.
	 * @param __window The window to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/11
	 */
	public DisplayIdentityScale(ScritchInterface __scritch,
		ScritchScreenBracket __screen,
		ScritchWindowBracket __window)
		throws NullPointerException
	{
		if (__scritch == null || __screen == null || __window == null)
			throw new NullPointerException("NARG");
		
		this.scritch = __scritch;
		this.screen = __screen;
		this.window = __window;
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
	 * @since 2024/03/09
	 */
	@Override
	public int screenX(int __x)
	{
		return __x;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int screenY(int __y)
	{
		return __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int textureH()
	{
		return this.scritch.window().contentHeight(this.window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/11
	 */
	@Override
	public int textureMaxH()
	{
		return this.scritch.screen().height(this.screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/11
	 */
	@Override
	public int textureMaxW()
	{
		return this.scritch.screen().width(this.screen);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/18
	 */
	@Override
	public int textureW()
	{
		return this.scritch.window().contentWidth(this.window);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureX(int __x)
	{
		return __x;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/09
	 */
	@Override
	public int textureY(int __y)
	{
		return __y;
	}
}
