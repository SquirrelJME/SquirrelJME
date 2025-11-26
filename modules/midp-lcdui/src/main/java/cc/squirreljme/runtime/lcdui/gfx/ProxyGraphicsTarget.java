// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Graphics;

/**
 * This is used for the target of any {@link ProxyGraphics}.
 *
 * @since 2022/02/25
 */
@SquirrelJMEVendorApi
public final class ProxyGraphicsTarget
{
	/** The target graphics object. */
	@SquirrelJMEVendorApi
	volatile Graphics _target;
	
	/**
	 * Initializes the target graphics.
	 * 
	 * @param __g The graphics to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/25
	 */
	@SquirrelJMEVendorApi
	public ProxyGraphicsTarget(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this._target = __g;
	}
	
	/**
	 * Returns the current {@link Graphics} target.
	 *
	 * @return The current {@link Graphics} target.
	 * @since 2025/11/25
	 */
	@SquirrelJMEVendorApi
	public Graphics getGraphics()
	{
		synchronized (this)
		{
			return this._target;
		}
	}
	
	/**
	 * Sets the graphics to draw onto.
	 * 
	 * @param __g The graphics to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/25
	 */
	@SquirrelJMEVendorApi
	public void setGraphics(Graphics __g)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._target = __g;
		}
	}
}
